Project - AWS Iaas for object recognition in video
SQS Queues : inputqueue, outputqueue
S3 Buckets : outputbucketnrcse546

Implementation : 
	1. We have 2 Tiers in the program - the Web-tier and the App-tier

	2. Web-Tier
		2.1. The Web tier deals with obtaining the request from the client using the public ip and putting the requests in the queue, and saves the messageID.
		2.2. It implements a load balancer that keeps polling approx number of messages from input queue and once it is above the threshold, which we have set as multiples of 3, a new App-Tier EC2 instance is created up to 19 instances.
		2.3. The webTier driver keeps polling the output queue and if the message body in output queue message matches the messageID saved, it retrieves the result from the S3 using the video name, also part of message body of the output queue. 
		2.4 It then passes the message result to the requested client.

	
	3. App-Tier
		3.1. The app tier deals with obtaining the video from raspberry pi and analyzing video for objects and saving result in S3.
		3.2. App tier Driver keeps polling the input queue, if there is any message in the input queue, it retrieves the message, gets the message id.
		3.3 After retrieving the message id, app tier downloads the video and analyzes it using deep leaning models and produces a result.
		3.4 The result is obtained by the app-tier, and saved into S3 with video name as key.
		3.5 The input message id appended with video name is sent as message body to output queue.
		3.6 If there are no other requests in the input queue, the app tier instance terminates itself.

How to run:
	•	Create the webtier jar by selecting the project and run as maven build. 
	•	Type package beside goal and run. The web tier jar is created in the project folder under target.
	•	Create an instance and copy the WebTier.1.0.0.jar, WebTier.1.0.0.jar.original, and the classes folder into the instance
	•	In the. Web tier instance run - java -jar WebTier.1.0.0.jar
	•	Set security group with inbound rules - All traffic + Ip – anywhere
	•	The credentials for the application are given in the /credentials/creds.docx file in the same folder. Copy the credentials into ~/.aws/credentials 
	•	Hit the web server using http://<public ip>/deeplearningvideo?requestvideo=getvideo
	•	Wait for the result to appear on the browser.


