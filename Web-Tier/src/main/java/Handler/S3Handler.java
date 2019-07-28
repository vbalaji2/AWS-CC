package Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import DataPersistence.Logger;

public class S3Handler {
private static final String BUCKET_REGION = "us-west-1";
	
	public static Bucket getBucket(String bucket_name) {
	
		AWSCredentials credentials = checkCredentials();

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
		Bucket named_bucket = null;
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket b : buckets) {
			if (b.getName().equals(bucket_name)) {
				named_bucket = b;
			}
		}
		return named_bucket;
	}

	public static Bucket createBucket(String bucket_name) {

		AWSCredentials credentials = checkCredentials();
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(BUCKET_REGION).build();
		Bucket b = null;
			try {
				Logger.getLogger().log("Creating Bucket : " + bucket_name);
				b = s3.createBucket(bucket_name);
			} catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage());
			}
		
		return b;
	}
	
	public static boolean isBucketExists(String bucket_name) {
		if(getBucket(bucket_name) != null) {
			return true;
		}
		
		Logger.getLogger().log("Bucket: " + bucket_name + " does not exist");
		return false;
	}
	
	public static void putObject(String bucket_name, String key_name, String file_path) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        try {
            s3.putObject(bucket_name, key_name, file_path);
        } catch (AmazonServiceException e) {
            Logger.getLogger().log(e.getErrorMessage());
            System.exit(1);
        }
    }
 
	public static String getObject(String bucket_name, String key) {		
		String result = "";
		AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();
		
		try {
			S3Object resultObject = s3client.getObject(bucket_name, key);
			S3ObjectInputStream inputStream = resultObject.getObjectContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	result += line;
		    }
		} catch(IOException ex1) {
			Logger.getLogger().log("Error while reading the output from S3");
		} catch(AmazonS3Exception ex2) {
			if(ex2.getErrorCode().equals("NoSuchKey")){
				Logger.getLogger().log("No key in bucket");
			}
		}
		
	    return result;
	}
 
 	public static void deleteObject(String bucket_name, String object_key) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
            s3.deleteObject(bucket_name, object_key);
        } catch (AmazonServiceException e) {
        	Logger.getLogger().log(e.getErrorMessage());
            System.exit(1);
        }
    }
  
    public static AWSCredentials checkCredentials(){
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            Logger.getLogger().log("Cannot load credentials for bucket functions");
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",e);
        }
        return credentials;
    }
}
