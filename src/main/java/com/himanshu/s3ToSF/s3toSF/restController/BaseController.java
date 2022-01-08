package com.himanshu.s3ToSF.s3toSF.restController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

	@GetMapping("/")
	public String getBase() {
		return "Hello from base URL";
	}
}
