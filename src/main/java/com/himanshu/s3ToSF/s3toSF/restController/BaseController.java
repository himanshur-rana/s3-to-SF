package com.himanshu.s3ToSF.s3toSF.restController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanshu.s3ToSF.s3toSF.service.S3ServiceClass;

@RestController
public class BaseController {

	
	Logger logger = LoggerFactory.getLogger(BaseController.class);
	@Autowired
	S3ServiceClass s3ServiceClassObj;
			
	@GetMapping("/")
	public String getBase() {
		logger.info("-------inside getHome method-----");
		return "Hello from base URL";
	}
	
	@GetMapping("/gets3")
	public String getS3File() {
		s3ServiceClassObj.getS3File();
		return "Done!";
	}
	
	@PostMapping("/postToSF")
	public String postToSF() {
		return null;
	}
}
