package Drivers;

import DataPersistence.DataStorage;
import DataPersistence.Logger;

public class WebTierDriver {
	
	DataStorage dataStorage;
	
	public String updateRequestToQueue(String requestInfo) {
		
		dataStorage = DataStorage.getObject();
		
		String result = null;
		String resultKey = null;
		String msgId = dataStorage.pushNewRequest(requestInfo);
		do {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Logger.getLogger().log(e.getMessage());
			}
			Logger.getLogger().log("Waiting for results for msgID :" +msgId);
			resultKey = dataStorage.getResultIfAvailable(msgId); // in sqs update the data storage
		}while(resultKey == null);
		
		result = dataStorage.getResultFromCloudStorage(resultKey);
		String keyValue = "["+resultKey + "," + result + "]";
		return keyValue;
	}
}
