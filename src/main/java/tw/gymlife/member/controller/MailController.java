package tw.gymlife.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.gymlife.member.service.MailService;

@Controller
public class MailController {
	
//	@Autowired
//	 private MailService mailService;
//	 
//	 @GetMapping("/mailTest")
//	 @ResponseBody
//	 public String hello(){
//		 	String recipient = "qqww576843@gmail.com";
//		    String subject = "Sample mail subject";
//		    String message = "This is a sample message for your mail.";
//		    mailService.prepareAndSend(recipient, subject, message);
//		    return "Mail sent";
//	 }
}