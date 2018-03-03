package com.github.dhaval_mehta.savetogoogledrive.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UploadController {

	@RequestMapping("/new_upload")
	public String newUplaod() {
		return "new_upload";
	}

	@RequestMapping("/uploads")
	public String uploads() {
		return "uploads";
	}

}
