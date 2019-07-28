package AppHandler;

import Handler.Constants;
import Handler.SQSHandler;

public class SQSAppHandler {
	 private static final String INPUT_QUEUE_NAME = Constants.INPUT_QUEUE_NAME;
	    private static final String OUTPUT_QUEUE_NAME = Constants.OUTPUT_QUEUE_NAME;
	    
	    public void createOutputQueue() {
	    	SQSHandler.createQueue(OUTPUT_QUEUE_NAME);
	    }
	    
	    public void enqueueDeepLearningOutput(String msgID, String videoName) {
	    	SQSHandler.enqueue(OUTPUT_QUEUE_NAME, msgID+"/"+videoName);
	    }
	    
	    public String getMessageFromQueue() {
	    	return SQSHandler.dequeue(INPUT_QUEUE_NAME);
	    }
}
