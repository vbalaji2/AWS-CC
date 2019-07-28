package Handler;

import java.util.List;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateName;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

public class EC2Handler {
	
	private RunInstancesResult result = null;
	
	public String createInstance(String imageID, int min, int max) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		RunInstancesRequest request = new RunInstancesRequest(imageID,min,max);
		request.setInstanceType("t2.micro");
		
		result = ec2.runInstances(request);
		return result.getReservation().getInstances().get(0).getInstanceId();
	}
	
	public void startInstance(String instanceID) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instanceID);
		ec2.startInstances(request);
	}
	
	public void stopInstance(String instanceID) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        StopInstancesRequest request = new StopInstancesRequest().
                withInstanceIds(instanceID);//stop instance using the instance id
        ec2.stopInstances(request);

	}
	
	public void terminateInstance(String instanceID) {
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        TerminateInstancesRequest request = new TerminateInstancesRequest().
                withInstanceIds(instanceID);//terminate instance using the instance id
        ec2.terminateInstances(request);

	}
	
	public int getInstanceCount() {
		int runningInstanceCount = 0;
		DescribeInstanceStatusRequest describeReq = new DescribeInstanceStatusRequest();
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		DescribeInstanceStatusResult instanceStatusResult = ec2.describeInstanceStatus(describeReq);
		List<InstanceStatus> statuses = instanceStatusResult.getInstanceStatuses();
		
		for(InstanceStatus status : statuses) {
			InstanceState state = status.getInstanceState();
			if(state.getName().equals(InstanceStateName.Running.toString())) {
				runningInstanceCount++;
			}
		}
		
		return runningInstanceCount;
	}
}
