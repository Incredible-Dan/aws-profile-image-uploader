package com.digysoft.awsimageupload.config;



import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig{

   @Bean
   public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
              "AKIA2M3GZDAWI5KDK3VH",
                "X36TKmm3a4Y0CjWjI7FTviUSA7fW7T/a05oSWCtf"
        );
        return AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-west-2")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
    }

    }