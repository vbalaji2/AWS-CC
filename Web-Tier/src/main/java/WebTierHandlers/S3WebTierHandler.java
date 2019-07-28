package WebTierHandlers;

import Handler.Constants;
import Handler.S3Handler;

public class S3WebTierHandler {

	public String getResultForKey(String key) {
		if(S3Handler.isBucketExists(Constants.BUCKET_NAME)) {
			return S3Handler.getObject(Constants.BUCKET_NAME,key);
		}
		
		return null;
	}
}
