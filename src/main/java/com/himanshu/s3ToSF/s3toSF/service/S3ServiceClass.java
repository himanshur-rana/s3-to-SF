package com.himanshu.s3ToSF.s3toSF.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.himanshu.s3ToSF.WrapperClasses.EmployeeWrapper;

@Service
public class S3ServiceClass {
	
	Logger logger = LoggerFactory.getLogger(S3ServiceClass.class);
	
	@Value("${s3.secretKey}")
	private String secretKey;
	@Value("${s3.accessKey}")
	private String accessKey;
	@Value("${s3.region}")
	private String region;
	@Value("${s3.bucketName}")
	private String bucketName;
	@Value("${s3.fileName}")
	private String fileName;
	@Value("${local.fileLocation}")
	private String localFileLocation;

	public String getS3File() {
		logger.info("----Inside getS3File method--------");
		AmazonS3 s3client = getS3Client();
		// keyname = file_YYYYMMDD.txt;
		Calendar cal = Calendar.getInstance();
		int date = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH);
		String sMonth = "";
		if(month < 9) {
			sMonth = '0' + String.valueOf(month + 1);
		} else {
			sMonth = String.valueOf(month + 1);
		}
		int year = cal.get(Calendar.YEAR);
		String keyName = fileName + "_" + year + sMonth + date + ".txt";
		logger.info("Printing keyName++" + keyName);
		S3Object o = s3client.getObject(bucketName, keyName);
		S3ObjectInputStream s3is = o.getObjectContent();
		logger.info("Retrieved from s3----" + s3is);
		FileOutputStream fos;
        try {
        	File file = new File(localFileLocation);
    		fos = new FileOutputStream(file);
            byte[] read_buf = new byte[1024];
            int read_len = 0;
            while ((read_len = s3is.read(read_buf)) > 0) {
                fos.write(read_buf, 0, read_len);
            }
            logger.info("file output stream " + fos);
            s3is.close();
            fos.close();
        } catch(IOException e) {
        	logger.error(e.getMessage());
        }
		return "Done!";
	}

	private AmazonS3 getS3Client() {
		logger.info("----Inside getS3Client method--------");
		logger.info("----AccessKey--------" + accessKey);
		logger.info("----secretKey--------" + secretKey);
		logger.info("----region--------" + region);
		logger.info("----region--------" + Regions.AP_SOUTH_1);
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		AmazonS3 s3client = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.AP_SOUTH_1)
				  .build();
		return s3client;
	}
	
	public List<EmployeeWrapper> readFileAndReturnResponse() {
		List<EmployeeWrapper> employeeWrapperList = new ArrayList<EmployeeWrapper>();
		try {
			FileReader fr = new FileReader(localFileLocation);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				logger.info("printing line" + line);
				List<String> record = Arrays.asList(line.split("\\|"));//splitting lines
				EmployeeWrapper empWrapperObj = new EmployeeWrapper();
				empWrapperObj.empId = record.get(0);
				empWrapperObj.name = record.get(1);
				empWrapperObj.dept = record.get(2);
				empWrapperObj.company = record.get(3);
				employeeWrapperList.add(empWrapperObj);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return employeeWrapperList;
	}
}
