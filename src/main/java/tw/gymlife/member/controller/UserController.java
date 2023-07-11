	package tw.gymlife.member.controller;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.PutMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.ResponseBody;
	
	import jakarta.servlet.http.HttpSession;
	import tw.gymlife.member.model.Member;
	import tw.gymlife.member.service.MemberService;
	
	@Controller
	public class UserController {
		
		@Autowired
		private MemberService memberService;
		
		
		@GetMapping("/userInfo")
		public String showFindUserDetails(HttpSession httpSession, Model model) {
			
			Object userId = httpSession.getAttribute("userId");
			Object userAccount = httpSession.getAttribute("userAccount"); 
		    Object userName = httpSession.getAttribute("userName");
		    Object userGender = httpSession.getAttribute("userGender"); 
		    Object userBirthDay = httpSession.getAttribute("userBirthDay");
		    Object userAddress = httpSession.getAttribute("userAddress");
		    Object userTel = httpSession.getAttribute("userTel");
		    Object userEmail = httpSession.getAttribute("userEmail");
		    Object userNickName = httpSession.getAttribute("userNickName");
	
		    model.addAttribute("userId", userId);
		    model.addAttribute("userAccount",userAccount);
		    model.addAttribute("userName", userName);
		    model.addAttribute("userGender", userGender);
		    model.addAttribute("userBirthDay", userBirthDay);
		    model.addAttribute("userAddress", userAddress);
		    model.addAttribute("userTel", userTel);
		    model.addAttribute("userEmail", userEmail);
		    model.addAttribute("userNickName", userNickName);
			
			return "frontgymlife/member/userInfo";
		}	
		
		 @ResponseBody
		 @PostMapping("/userInfo")
		    public ResponseEntity<Member> updateUserDetails(@RequestBody Member member,HttpSession httpSession) {
		        Member updatedMember = memberService.updateUserDetails(
		            member.getUserId(), 
		            member.getUserAccount(), 
		            member.getUserName(), 
		            member.getUserGender(), 
		            member.getUserAddress(), 
		            member.getUserBirthDay(), //json限定日期格式 之後修改
		            member.getUserTel(), 
		            member.getUserEmail(), 
		            member.getUserNickName()
		        );
		        
		        if(updatedMember == null){
		            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		        }
		        //更新好後再將值設定到session裡面
		        httpSession.setAttribute("userId", updatedMember.getUserId());
		        httpSession.setAttribute("userAccount", updatedMember.getUserAccount());
		        httpSession.setAttribute("userName", updatedMember.getUserName());
		        httpSession.setAttribute("userGender", updatedMember.getUserGender());
		        httpSession.setAttribute("userAddress", updatedMember.getUserAddress());
		        httpSession.setAttribute("userBirthDay", updatedMember.getUserBirthDay());
		        httpSession.setAttribute("userTel", updatedMember.getUserTel());
		        httpSession.setAttribute("userEmail", updatedMember.getUserEmail());
		        httpSession.setAttribute("UserNickName", updatedMember.getUserNickName());
		        
		        
		        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
		    }	
		}
	
