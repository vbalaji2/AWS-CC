package AppHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAppHandler {
	public static String downloadAndGetFile(String fileurl, String dir) throws IOException{
		String fileName = null;
		URL url = new URL(fileurl);
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
	    int responseCode = httpConn.getResponseCode();
	    
	    if(responseCode == HttpURLConnection.HTTP_OK) {
	        String disposition = httpConn.getHeaderField("Content-Disposition");
	          
	 
	            if (disposition != null) {
	                // extracts file name from header field
	            int index = disposition.indexOf("filename=");
	            if (index > 0) {
	                fileName = disposition.substring(index + 9,
	                        disposition.length());
	            }
	        }
	        
	        // opens input stream from the HTTP connection
	        InputStream inputStream = httpConn.getInputStream();
	        String saveFilePath = dir + File.separator + fileName;
	         
	        // opens an output stream to save into file
	            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
	 
	            int bytesRead = -1;
	            byte[] buffer = new byte[4096];
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	 
	            outputStream.close();
	            inputStream.close();
	 
	            System.out.println("File downloaded");
	    } else {
	        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
	    }
	    httpConn.disconnect();
	    
	    return fileName;
		}
}
