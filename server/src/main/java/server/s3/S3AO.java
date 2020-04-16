package server.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class S3AO extends AbstractS3AO {
    @Override
    public void uploadPhoto(String alias, String key, URL url) throws IOException {
        // Write code to do the following:
            // 1. get name of file to be copied
            System.out.println("Attempting to upload photo to s3...");

            File file = new File("/tmp/temporary.jpeg");
            FileUtils.copyURLToFile(url, file);
            // 2. get name of S3 bucket from the command line
            // 3. upload file to the specified S3 bucket using the file name as the S3 key
            PutObjectRequest por = new PutObjectRequest(BUCKET_NAME, key, file);
            por.setCannedAcl(CannedAccessControlList.PublicReadWrite);
            PutObjectResult result = S3_CLIENT.putObject(por/*bucket, key, file*/);
            file.deleteOnExit();
            System.out.println("Uploaded photo to s3");
    }

    @Override
    public String getProfilePicFromS3(String key) throws IOException {
            // Get an object and print its contents.
            System.out.println("Downloading an object...");
            S3Object fullObject = new S3Object();
            URL url;
            try {
                fullObject = S3_CLIENT.getObject(new GetObjectRequest(BUCKET_NAME, key));
                //System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
                //System.out.println("Content: ");
                URI uri = fullObject.getObjectContent().getHttpRequest().getURI();
                url = uri.toURL();

            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw e;
            } finally {
                // To ensure that the network connection doesn't remain open, close any open input streams.
                if (fullObject != null) {
                    fullObject.close();
                }
            }
            System.out.printf("URL = %s\n", url.toString());
            return url.toString();
    }

}
