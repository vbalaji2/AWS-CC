package DataPersistence;

import java.util.HashMap;
import java.util.Map;

import WebTierHandlers.S3WebTierHandler;
import WebTierHandlers.SQSWebTierHandler;

public class DataStorage {
	private Map<String,String> msgIdToName = null;
	private static DataStorage dataStorage = null;
	private SQSWebTierHandler sqsHandler;
	private S3WebTierHandler s3Handler;

	private DataStorage() {
		msgIdToName = new HashMap();
		sqsHandler = new SQSWebTierHandler();
		s3Handler = new S3WebTierHandler();
	}
	
	public static DataStorage getObject() {
		if(dataStorage == null) {
			dataStorage = new DataStorage();
		}
		return dataStorage;
	}
	
	public void addResultToStorage(String[] result) {
		if(result != null) {
			msgIdToName.put(result[0], result[1]);
		}
	}
	
	public String getResultIfAvailable(String msgId) {
		
		String result = null;
		if(sqsHandler.isOutputAvailable()) {
			Logger.getLogger().log("OUPUT QUEUE IS AVAILABLE");
			addResultToStorage(sqsHandler.dequeResult());
		}
		
		 if(msgIdToName.containsKey(msgId)) {
			 result = msgIdToName.get(msgId);
			 msgIdToName.remove(msgId);
		 }
		 
		 return result;
	}
	
	public String pushNewRequest(String requestInfo) {
		return sqsHandler.enqueueRequest(requestInfo);
	}
	
	public String getResultFromCloudStorage(String key) {
		return s3Handler.getResultForKey(key);
	}
}
