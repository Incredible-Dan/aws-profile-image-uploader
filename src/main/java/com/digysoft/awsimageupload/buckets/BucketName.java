package com.digysoft.awsimageupload.buckets;

public enum BucketName {
    PROFILE_IMAGGE("my-first-aws-bucket-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
