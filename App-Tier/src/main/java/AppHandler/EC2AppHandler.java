package AppHandler;

import com.amazonaws.util.EC2MetadataUtils;

import Handler.EC2Handler;

public class EC2AppHandler {
	public void terminateInstance() {
		System.out.println("App terminating");
		try {
		EC2Handler ec2Handler = new EC2Handler();
		String instanceID = EC2MetadataUtils.getInstanceId();
		ec2Handler.terminateInstance(instanceID);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
