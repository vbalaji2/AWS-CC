package Driver;

import Handler.Constants;
import AppHandler.HttpAppHandler;

public class VideoRecorder {
	private String videoName = null;
	private final static String URL = Constants.VIDEO_REQUEST_URL;
	
	//PATH should be the path on instance
	private final static String PATH = Constants.VIDEO_PATH;
	
	public String getFileName() {
		return videoName;
	}
	
	public String downloadAndGetfile() {
		String filePath = null;
		try {
			videoName = HttpAppHandler.downloadAndGetFile(URL,PATH);
			filePath = PATH + "/" + videoName;
		}
		catch(Exception ex) {
			System.out.println("Exception in downloading file : " + ex.getMessage() );
		}
		return filePath;
	}
}
