package AppHandler;

import Handler.Constants;
import Handler.S3Handler;

public class S3AppHandler {
private final String BUCKET_NAME = Constants.S3_OUTPUT_BUCKET_NAME;
	
	public void putObject(String videoName, String result) {
		
		if(!S3Handler.isBucketExists(BUCKET_NAME)) {
			S3Handler.createBucket(BUCKET_NAME);
		}
		
		S3Handler.putObject(BUCKET_NAME, videoName, result);
		
	}
}
