package server.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.net.URL;

abstract public class AbstractS3AO {
    protected static AmazonS3 S3_CLIENT = AmazonS3ClientBuilder
            .standard()
            .withRegion(Regions.US_WEST_2)
            //.withCredentials(new ProfileCredentialsProvider())
            .build();
    protected static String BUCKET_NAME = "tweeter-photos";

    abstract public void uploadPhoto(String alias, String urlStr, URL url) throws IOException;
    abstract public String getProfilePicFromS3(String imageName) throws IOException;
    }
