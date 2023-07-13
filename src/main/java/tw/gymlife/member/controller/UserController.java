	package tw.gymlife.member.controller;
	
	import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.PutMapping;
	import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;

import jakarta.servlet.http.HttpSession;
	import tw.gymlife.member.model.Member;
	import tw.gymlife.member.service.MemberService;
	
	@Controller
	public class UserController {
		
		@Autowired
		private MemberService memberService;
		
		@Autowired
		private SimpMessagingTemplate template;
		
		
		@GetMapping("/userInfo")
		public String showFindUserDetails(HttpSession httpSession, Model model) {
			
			Object userId = httpSession.getAttribute("userId");
			Member member = memberService.findById((int) userId).orElse(null);
			if (member != null) {
				  	model.addAttribute("userId",member.getUserId());
				    model.addAttribute("userAccount",member.getUserAccount());
				    model.addAttribute("userName", member.getUserName());
				    model.addAttribute("userGender", member.getUserGender());
				    model.addAttribute("userBirthDay", member.getFormattedUserBirthDay());
				    model.addAttribute("userAddress", member.getUserAddress());
				    model.addAttribute("userTel", member.getUserTel());
				    model.addAttribute("userEmail", member.getUserEmail());
				    model.addAttribute("userNickName", member.getUserNickName());
				    if(member.getUserPhoto() != null){
			            String encodedImage = Base64.getEncoder().encodeToString(member.getUserPhoto());
			            model.addAttribute("userPhoto", encodedImage);
			        }
			}
			
			return "frontgymlife/member/userInfo";
		}	
		
		 @ResponseBody
		 @PostMapping("/userInfo")
		    public ResponseEntity<Member> updateUserDetails(@RequestParam("userId") int userId,
		    		@RequestParam("userAccount") String userAccount,
		    		@RequestParam("userName") String userName,
		    		@RequestParam("userGender") String userGender,
		    		@RequestParam("userBirthDay") @DateTimeFormat(pattern="yyyy-MM-dd") Date userBirthDay,
		    		@RequestParam("userAddress") String userAddress,
		    		@RequestParam("userTel") String userTel,
		    		@RequestParam("userEmail") String userEmail,
		    		@RequestParam("userNickName") String userNickName,
		    		@RequestParam("userPhoto") MultipartFile userPhoto,
		    		HttpSession httpSession,
		    		Model model) {
		       
			 if (!userPhoto.isEmpty()) {
		            try {
		                byte[] photoData = userPhoto.getBytes();

		                // Update the member object with the photo data
		                Member member = new Member();
		                member.setUserId(userId);
		                member.setUserAccount(userAccount);
		                member.setUserName(userName);
		                member.setUserGender(userGender);
		                member.setUserBirthDay(userBirthDay);
		                member.setUserAddress(userAddress);
		                member.setUserTel(userTel);
		                member.setUserEmail(userEmail);
		                member.setUserNickName(userNickName);
		                member.setUserPhoto(photoData);
		                
		                Member updatedMember = memberService.updateUserDetails( member.getUserId(),
		                	    member.getUserAccount(),
		                	    member.getUserName(),
		                	    member.getUserGender(),
		                	    member.getUserAddress(),
		                	    member.getUserBirthDay(),
		                	    member.getUserTel(),
		                	    member.getUserEmail(),
		                	    member.getUserNickName(),
		                	    member.getUserPhoto()
		                	    );
		                
		                
		                if(updatedMember == null){
		                	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		                }
		                List<Member> allMembers = memberService.selectAllMembers();
		                template.convertAndSend("/topic/MemberQuery", allMembers);
		                return new ResponseEntity<>(updatedMember, HttpStatus.OK);
		            
		            } catch (IOException e) {
		                // Handle the exception appropriately
		            }	
		        }
			 
			    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		        
		    }	
		 
			
			 @GetMapping("/userPhoto/{userId}")
			 public ResponseEntity<byte[]> getUserPhoto(@PathVariable("userId") int userId) {
			     Member member = memberService.findById(userId).orElse(null);
			     if (member != null && member.getUserPhoto() != null) {
			         return ResponseEntity.ok()
			             .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
			             .body(member.getUserPhoto());
			     } else {
			         return ResponseEntity.notFound().build();
			     }
			 }

		}
	
