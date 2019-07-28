package Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import DataPersistence.Logger;

public class SQSHandler {
	 public static void createQueue(String queueName) {
		 Logger.getLogger().log("Creating Queue");
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        try {
	            CreateQueueRequest createQueueRequest =
	                    new CreateQueueRequest(queueName);
	            String myQueueUrl = sqs.createQueue(createQueueRequest)
	                    .getQueueUrl();
	            System.out.println(myQueueUrl);
	        } catch (AmazonSQSException e) {
	            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
	            	Logger.getLogger().log("Craeting queue that already exists : " + queueName);
	            }
	        }
	    }

	    public static boolean isQueueExists(String queueName) {
	    	AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	    	try {
	    		String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	    	} catch(AmazonSQSException ex) {
	    		if(ex.getErrorCode().equals("AWS.SimpleQueueService.NonExistentQueue")) {
	    			Logger.getLogger().log("Queue doesnot Exist : " + queueName);
	    			return false;
	    		}
	    	}
	    	
	    	return true;
	    }
	    
	    public static String enqueue(String queueName, String msg) {
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        
	        if(!isQueueExists(queueName)) {
	        	Logger.getLogger().log("Queue is being created");
	        	createQueue(queueName);
	        }
	        
	        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	        SendMessageRequest send_msg_request = new SendMessageRequest()
	                .withQueueUrl(queueUrl)
	                .withMessageBody(msg)
	                .withDelaySeconds(5);
	        SendMessageResult result = sqs.sendMessage(send_msg_request);
	     
	        return result.getMessageId();
	    }

	    public static String dequeue(String queueName) {
	    	Message message = null;
	    	
	        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
			receiveMessageRequest.setMaxNumberOfMessages(1);
			receiveMessageRequest.setWaitTimeSeconds(10);
			ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
			List<Message> messages = receiveMessageResult.getMessages();
			
	        if(messages != null && messages.size() > 0) {
	        	
	        	message = messages.get(0);
	        }
	        else {
	        	Logger.getLogger().log("No messages in queue : "+ queueName);
	        }
	        
	        if(message != null) {
	        	sqs.deleteMessage(queueUrl, message.getReceiptHandle());
	        }
	        
	        return (message != null) ? message.getBody() : null;
	    }
	    
	    public static Integer getNumberOfMsgsInSQS(String queueName) {
	    	final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
	        
	        List<String> attributeNames = new ArrayList<String>();
			attributeNames.add("ApproximateNumberOfMessages");
	        GetQueueAttributesRequest attrReq = new GetQueueAttributesRequest(queueUrl, attributeNames);
	        GetQueueAttributesResult qresult = sqs.getQueueAttributes(attrReq); 
	        Map attributes = qresult.getAttributes();
	        String approxNumber = (String)attributes.get("ApproximateNumberOfMessages");
	        
	        Logger.getLogger().log(queueName + " " +approxNumber);
	    	
	        return (approxNumber != null) ? Integer.parseInt(approxNumber) : 0;
	    }
}
