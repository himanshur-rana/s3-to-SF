package com.himanshu.s3ToSF.s3toSF.restController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.himanshu.s3ToSF.WrapperClasses.EmployeeWrapper;
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
	
	@GetMapping("/getS3File")
	public List<EmployeeWrapper> postToSF() {
		s3ServiceClassObj.getS3File();
		return s3ServiceClassObj.readFileAndReturnResponse();
	}
}
