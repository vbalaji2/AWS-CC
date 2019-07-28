package Drivers.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import DataPersistence.Logger;
import Drivers.WebTierDriver;

@RestController
@EnableAutoConfiguration
public class HttpController {
	@RequestMapping("/")
	public String justAnExample() {
		return "Hello world";
	}
	
	@RequestMapping(value = "info", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getInfo() {
		return new ResponseEntity<String>("Service up!", HttpStatus.OK);
	}
	
	@RequestMapping(value = "log", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getLog() {
		return new ResponseEntity<String>(Logger.getLogger().getLoggerMsg(), HttpStatus.OK);
	}
	@RequestMapping(value = "deeplearningvideo", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> inputURL(@RequestParam("requestvideo") String requestInfo) {
		Logger.getLogger().log("New http request from user " + requestInfo);
		String outputMessage="";
		outputMessage += new WebTierDriver().updateRequestToQueue(requestInfo);
		return new ResponseEntity<String>(outputMessage, HttpStatus.OK);
	}
}
