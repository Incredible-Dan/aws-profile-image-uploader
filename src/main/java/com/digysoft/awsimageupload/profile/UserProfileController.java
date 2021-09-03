package com.digysoft.awsimageupload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {

    private final USerProfileService uSerProfileService;

    @Autowired
    public UserProfileController(USerProfileService uSerProfileService){
        this.uSerProfileService = uSerProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles(){
        return uSerProfileService.getUserProfiles();
    }


    @PostMapping(
            path = "{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileId") UUID userProfileID,
                                        @RequestParam("file")MultipartFile file){
        uSerProfileService.uploadUserProfileImage(userProfileID,file);

    }
    @GetMapping("{userProfileId}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileId") UUID userProfileID){
        return uSerProfileService.downloadUserProfileImage(userProfileID);
    }
}
