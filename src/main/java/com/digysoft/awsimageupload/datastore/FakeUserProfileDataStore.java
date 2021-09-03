package com.digysoft.awsimageupload.datastore;

import com.digysoft.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("206b3453-b049-44e1-95aa-fb89ef576743"), "User_Profile 1", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("001ed84b-6fa7-4d09-b24b-bca88005d23e"), "User_Profile 2", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
