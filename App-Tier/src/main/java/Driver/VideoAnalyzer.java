package Driver;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class VideoAnalyzer {
public static void RunCommands(String videoPath) {
        
    }

	public static String AnalyzeVideo(String filePath){
		String result = "";
		
		/*
		 * Here call the deep learning module with a script 
		 * in script write these lines:
		 * 
		 * /bin/bash
		 * $ Xvfb :1 & export DISPLAY=:1
		 * $ ./darknet detector demo cfg/coco.data cfg/yolov3-tiny.cfg + weight_path + video_path (filePath)
		 * $ python darknet_test.py
		 * 
		 * get the result in result file and send it
		 * */
		
		
		Process p;
        try {
            String[] cmdA = {"/home/ubuntu/darknet/darknet_script.sh",filePath};
            p = new ProcessBuilder(cmdA).start();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));

            String line;  
                while ((line = in.readLine()) != null) {  
                    result+=line;  
                }  
                in.close();
                p.waitFor();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
	        
		return result;
	}
}
