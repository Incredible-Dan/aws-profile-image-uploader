package com.digysoft.awsimageupload.profile;

import com.digysoft.awsimageupload.buckets.BucketName;
import com.digysoft.awsimageupload.fileStore.FileStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

@Slf4j
@Service
public class USerProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public USerProfileService(UserProfileDataAccessService userProfileDataAccessService,
                              FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    void uploadUserProfileImage(UUID userProfileID, MultipartFile file) {
        //     1.   check if image is not empty
        isFileEmpty(file);
        //   2.     check if file is an image

        isImage(file);

        UserProfile user = getUserProfileOrThrow(userProfileID);

        //    4.    grab some metadata from file if any

        Map<String, String> metadata = extractMetadata(file);

        //    5.    store the image in S3 and update database (userProfileImageLink with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGGE.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(),UUID.randomUUID());

        try{
            fileStore.save(path,filename,Optional.of(metadata),file.getInputStream());
            user.setUserProfileImageLink(filename);
        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    byte[] downloadUserProfileImage(UUID userProfileID) {
        UserProfile user = getUserProfileOrThrow(userProfileID);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGGE.getBucketName(),
                user.getUserProfileId());
        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileID) {
        //    3.    check if user exists in our database

        log.info("up1 --> {} ", userProfileID);

        userProfileDataAccessService.getUserProfiles()
                .forEach(up -> log.info("prof --> {}", up.getUserProfileId()));

        UserProfile user = userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileID))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found", userProfileID)));
        return user;
    }

    private void isImage(MultipartFile file) {
        if(!Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("file must be an image [" + file.getContentType() +"]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload an empty file[" + file.getSize() + "]");
        }
    }


}
