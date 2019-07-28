package Handler;

import com.amazonaws.regions.Regions;

public class Constants {
	
	public static final String ACCESSKEY = "*****";

	public static final String SECRETKEY = "*****";

	public static final Regions REGION = Regions.US_WEST_1;

	public static final String FORMAT = "json";

	public static final String INPUT_QUEUE_NAME = "inputqueue";

	public static final String OUTPUT_QUEUE_NAME = "outputqueue";

	public static final String SECURITYGROUPID = "*****";

	public static final Integer MAX_RUNNING_INSTANCES = 19;

	public static final String APP_INSTANCE_AMI = "ami-0a13859acbff33ad7";
	
	public static final Integer LOAD_BALANCER_THRESHOLD = 3;
	
	public static final String LOGGERFILE = "log.txt";
	
	public static final String BUCKET_NAME = "outputbucketnrcse546";
	
	public static final Integer LOAD_BALANCER_SLEEP_TIME = 4000;

}
