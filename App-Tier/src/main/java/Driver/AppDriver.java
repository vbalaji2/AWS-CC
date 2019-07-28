package Driver;

import AppHandler.EC2AppHandler;
import AppHandler.S3AppHandler;
import AppHandler.SQSAppHandler;
import Driver.VideoAnalyzer;
import Driver.VideoRecorder;

public class AppDriver {
	private SQSAppHandler sqsHandler;
	private S3AppHandler s3Handler;
	private EC2AppHandler ec2Handler;

	public AppDriver() {
		sqsHandler = new SQSAppHandler();
		ec2Handler = new EC2AppHandler();
		s3Handler = new S3AppHandler();
	}
	
	public void driveApp() {
		//check if queue is empty instead of true
		while(true) {
			
			sqsHandler = new SQSAppHandler();
			String msgId = sqsHandler.getMessageFromQueue();
			if(msgId == null) break;

			VideoRecorder videoRecorder = new VideoRecorder();
			String path = videoRecorder.downloadAndGetfile();
			
			if(path != null) {
				String result = VideoAnalyzer.AnalyzeVideo(path);
				System.out.println("Result :" + result);
				s3Handler.putObject(videoRecorder.getFileName(), result);
				sqsHandler.enqueueDeepLearningOutput(msgId, videoRecorder.getFileName());
			}
			else{
				System.out.println("Path is null :O");
			}
			
		}
		//System.out.println("TODO ---- Teriminate instance, nothing in queue ---- TODO");
		ec2Handler.terminateInstance();
	}
}
