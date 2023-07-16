	package tw.gymlife.member.controller;
	
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
	
	import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MemberService;
	
	@Controller
	public class MemberController {
		
		  @Autowired
		    private MemberService memberService;
		  
		  @Autowired
		  private SimpMessagingTemplate template;
	
		    @GetMapping("/MemberQuery")
		    public String showMemberQuery(Model model) {
		        model.addAttribute("members", memberService.selectAllMembers());
		        return "BackGYMLIFE/member/memberList";
		    }
		    
		    @GetMapping("gymlife/api/MemberQuery")
		    @ResponseBody
		    public List<Member> getMembers() {
		     List<Member> selectAllMembers = memberService.selectAllMembers();
		     return selectAllMembers;
		    }	
		    
		    @DeleteMapping("gymlife/api/DeleteMember/{userId}")	
		    @ResponseBody
		    public ResponseEntity<?> deleteMember(@PathVariable int userId) {
		        try {
		            memberService.deleteMember(userId);
		            return new ResponseEntity<>(HttpStatus.OK);
		        } catch (Exception e) {
		            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    }
	
		    @PostMapping("gymlife/api/memberUpdate")
		    public ResponseEntity<Member> memberUpdate(@RequestBody Member body) {
		        try {
		            Member member = memberService.memberUpdate(
		                body.getUserId(),
		                body.getUserAccount(),
		                body.getUserName(),
		                body.getUserGender(),
		                body.getUserAddress(),
		                body.getUserTel(),
		                body.getUserEmail(),
		                body.getUserBirthDay()
		            );
//		            List<Member> selectAllMembers = memberService.selectAllMembers();
		            return new ResponseEntity<>(member, HttpStatus.OK);

		        } catch (Exception e) {
		            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    }
		    
		    @PostMapping("api/updateUserStatus")
		    public ResponseEntity<Member> updateUserStatus(@RequestBody Member body){
		    try {
		    	Member member = memberService.updateUserStatus(
		    		body.getUserId(),
		    		body.getUserStatus()
		    		);
					    return new ResponseEntity<>(member, HttpStatus.OK);
			
			    } catch (Exception e) {
			        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			    }
			}
		    
		    @GetMapping("/memberAnalyze")
		    public String memberAnalyze() {
				return "backgymlife/member/memberAnalyze";
			}
		    
		    @GetMapping("/api/genderCountData")
		    @ResponseBody
		    public Map<String, Integer> getGenderCountData() {
		        return memberService.getGenderCount();
		    }
		    
		    @GetMapping("/api/totalMember")
		    @ResponseBody
		    public Long getTotalUsers() {
		      long totalMembers = memberService.countMember();
		      return totalMembers;
		    }
		    
		    @GetMapping("/api/membersByMonth")
		    @ResponseBody
		    public List<Map<String, Object>> getMembersByMonth() {
		        return memberService.countMembersByMonth();
		    }
		   
		    
		    	
		    
		    public void onDataChange() {
		        // When data changes, fetch the new data
		        List<Member> selectAllMembers = memberService.selectAllMembers();
		        // Send the new data to all connected clients
		        template.convertAndSend("/topic/MemberQuery", selectAllMembers);
		        
		    }
		
	}
