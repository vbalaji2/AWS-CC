package Handler;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;

public class S3Handler {
	public static Bucket getBucket(String bucket_name) {

		AWSCredentials credentials = checkCredentials();

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.build();
		Bucket named_bucket = null;
		List<Bucket> buckets = s3.listBuckets();
		for (Bucket b : buckets) {
			System.out.println("Bucket name : " + b.getName());
			if (b.getName().equals(bucket_name)) {
				named_bucket = b;
			}
		}
		return named_bucket;
	}

	public static Bucket createBucket(String bucket_name) {

		AWSCredentials credentials = checkCredentials();

		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion("us-west-1").build();
		Bucket b = null;
		if (s3.doesBucketExistV2(bucket_name)) {
			System.out.format("Bucket %s already exists.\n", bucket_name);
			b = getBucket(bucket_name);
		} else {
			try {
				b = s3.createBucket(bucket_name);
			} catch (AmazonS3Exception e) {
				System.err.println(e.getErrorMessage());
			}
		}
		return b;
	}
	
	public static boolean isBucketExists(String bucket_name) {
		
		if(getBucket(bucket_name) != null) {
			System.out.println("BUCKET EXISTS");
			return true;
		}
		return false;
	}
	
	public static void putObject(String bucket_name, String key_name, String file_path) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        try {
        	System.out.println("Putting (" + key_name + ", " + file_path + ") in bucket " + bucket_name);
            s3.putObject(bucket_name, key_name, file_path);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
 
 	public static void deleteObject(String bucket_name, String object_key) {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        try {
        	System.out.println("Deleting " + object_key + " from bucket " + bucket_name);
            s3.deleteObject(bucket_name, object_key);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }
  
    public static AWSCredentials checkCredentials(){
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",e);
        }
        return credentials;
    }
}
