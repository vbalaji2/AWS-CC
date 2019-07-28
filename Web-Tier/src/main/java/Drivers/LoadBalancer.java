package Drivers;

import DataPersistence.Logger;
import Handler.Constants;
import WebTierHandlers.SQSWebTierHandler;
import WebTierHandlers.EC2WebTierHandler;

public class LoadBalancer {
    
    private EC2WebTierHandler ec2Handler;
    private SQSWebTierHandler sqsHandler;
    
	public void loadBalance() {
		
		int threshold = Constants.LOAD_BALANCER_THRESHOLD;
		int maxInstance = Constants.MAX_RUNNING_INSTANCES;
		
		ec2Handler = new EC2WebTierHandler();
		sqsHandler = new SQSWebTierHandler();
		
		while(true) {
			Integer numMsgs = sqsHandler.getNoOfRequestsFromQueue();
			
			Integer countRunningInstances = ec2Handler.getNumberOfInstances();
			
			Integer numAppInstances = countRunningInstances - 1;
			
			Integer reqAppInstances = (numMsgs / threshold) + 1;
			
			if(numMsgs > 0 || reqAppInstances <= numAppInstances) {
				
				if(reqAppInstances > numAppInstances && reqAppInstances < maxInstance) {
					Logger.getLogger().log("Creating app instances" + (reqAppInstances - numAppInstances));
					ec2Handler.createInstance(reqAppInstances - numAppInstances);
				}
				else if(reqAppInstances > numAppInstances && reqAppInstances >= maxInstance) {
					Logger.getLogger().log("Creating app instances" + (maxInstance - numAppInstances));
					ec2Handler.createInstance(maxInstance - numAppInstances);
				}
			}
			else {
				try {
					Thread.sleep(Constants.LOAD_BALANCER_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e){
				Logger.getLogger().log(e.getMessage());
				
			}
			
		}
	}
}
