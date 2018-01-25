package com.zero.zeros.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zero.zeros.common.service.CommonService;

@Controller
public class CommonController {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping("/")
    public String index(Model model) {
		System.out.println("index call");
		model.addAttribute("list", commonService.selectMenuList());
    	return "index";
    }

}
