package Handler;

import java.util.List;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSHandler {
	 public static void createQueue(String queueName) {
		 	System.out.println("Creating Queue: " + queueName);
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        try {
	            CreateQueueRequest createQueueRequest =
	                    new CreateQueueRequest(queueName);
	            String myQueueUrl = sqs.createQueue(createQueueRequest)
	                    .getQueueUrl();
	            System.out.println(queueName + " URL : " + myQueueUrl);
	        } catch (AmazonSQSException e) {
	            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
	            	System.out.println("Queue already exists");
	                throw e;
	            }
	        }
	    }

	    public static boolean isQueueExists(String queueName) {
	    	AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	    	try {
	    		String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	    	} catch(AmazonSQSException ex) {
	    		if(ex.getErrorCode().equals("AWS.SimpleQueueService.NonExistentQueue")) {
	    			return false;
	    		}
	    	}
	    	System.out.println("Queue " + queueName + " exists");
	    	return true;
	    }
	    
	    public static void enqueue(String queueName, String msg) {
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        
	        if(!isQueueExists(queueName)) {
	        	System.out.println("Queue is being created");
	        	createQueue(queueName);
	        }
	        
	        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	        SendMessageRequest send_msg_request = new SendMessageRequest()
	                .withQueueUrl(queueUrl)
	                .withMessageBody(msg)
	                .withDelaySeconds(5);
	        sqs.sendMessage(send_msg_request);
	        System.out.println("enqueued msg " + msg + "in " + queueName);
	    }

	    public static String dequeue(String queueName) {
	    	Message message = null;
	    	
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
	        
	        if(messages != null && messages.size() > 0) {
	        	System.out.println("Size > 0");
	        	message = messages.get(0);
	        }
	        // delete messages from the queue
	        
	        if(message != null) {
	        	sqs.deleteMessage(queueUrl, message.getReceiptHandle());
	        	System.out.println("Received : " + message.getMessageId());
	        }
	        
	        return (message != null) ? message.getMessageId() : null;
	    }
}
