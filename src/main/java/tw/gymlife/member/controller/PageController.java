package tw.gymlife.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {
	
	@GetMapping("/")
	public String home() {
		return "frontgymlife/member/firstPage";
	}
	
	@GetMapping("/Admin")
	public String admin(HttpSession session) {	
		Object userPermission = session.getAttribute("userPermission");
		if(userPermission != null & !userPermission.toString().equals("1")) {
			 return "frontgymlife/member/firstPage";
		}
		
		return "backgymlife/member/BackEnd";
	}
	
	
}
