package WebTierHandlers;

import DataPersistence.Logger;
import Handler.Constants;
import Handler.EC2Handler;

public class EC2WebTierHandler {
	
	private static final String AMI_ID = Constants.APP_INSTANCE_AMI;
	private EC2Handler ec2Handler;
	
	public EC2WebTierHandler() {
		ec2Handler = new EC2Handler();
	}
	
	public void createInstance(int noOfInstances){
//		int min = (noOfInstances > 1) ? noOfInstances -1 : noOfInstances;
//		ec2Handler.createInstance(AMI_ID, min, noOfInstances);
		
		for(int i=0; i<noOfInstances; i++) {
			Integer countRunningInstances = getNumberOfInstances();
			Integer numAppInstances = countRunningInstances - 1;
			Integer currAppInstances = numAppInstances;
			
			ec2Handler.createInstance(AMI_ID, 1, 1);
			
			while(currAppInstances == numAppInstances)
			{
				try {
					Thread.sleep(2000);
				} catch(InterruptedException ex) {
					Logger.getLogger().log("Error in ec2 web tier handler : " + ex.getMessage());
				}
				countRunningInstances = getNumberOfInstances();	
				numAppInstances = countRunningInstances - 1;
			}
		}
		
	}
	
	public int getNumberOfInstances() {
		return ec2Handler.getInstanceCount();
	}
}
