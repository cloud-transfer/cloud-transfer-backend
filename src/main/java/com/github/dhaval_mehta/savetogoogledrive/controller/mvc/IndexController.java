package com.github.dhaval_mehta.savetogoogledrive.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("")
	public String index() {
		return "index";
	}
}
