package WebTierHandlers;

import Handler.Constants;
import Handler.SQSHandler;

public class SQSWebTierHandler {

	private static final String INPUT_QUEUE_NAME = Constants.INPUT_QUEUE_NAME;
    private static final String OUTPUT_QUEUE_NAME = Constants.OUTPUT_QUEUE_NAME;
    
    public String enqueueRequest(String msg) {
    	if(!SQSHandler.isQueueExists(INPUT_QUEUE_NAME)) {
    		SQSHandler.createQueue(INPUT_QUEUE_NAME);
    	}
    	return SQSHandler.enqueue(INPUT_QUEUE_NAME, msg);
    }
	
    public String[] dequeResult() {
    	String output = SQSHandler.dequeue(OUTPUT_QUEUE_NAME);
    	String[] result = null;
    	if(output != null) {
    		System.out.println("RESULT FROM OUTPUT QUEUE : " + output);
    		result = output.split("/");
    	}
    	return result;
    }
    
    public boolean isOutputAvailable() {
    	return (SQSHandler.isQueueExists(OUTPUT_QUEUE_NAME));
    }
    
    public int getNoOfRequestsFromQueue() {
    	if(SQSHandler.isQueueExists(INPUT_QUEUE_NAME)) {
    		return SQSHandler.getNumberOfMsgsInSQS(INPUT_QUEUE_NAME);
    	}else {
    		return 0;
    	}
    }
}
