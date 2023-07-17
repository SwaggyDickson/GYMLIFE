package tw.gymlife.member.controller;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import jakarta.servlet.http.HttpSession;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MailService;
import tw.gymlife.member.service.MemberService;
import tw.gymlife.member.service.ReCaptchaService;

	@Controller
	public class AuthController {
			
		@Autowired 
		private MemberService memberService;
		
		@Autowired
		private MailService mailService;
		
		@Autowired
		private  PasswordEncoder pwdEncoder;
		
		@Autowired
		private ReCaptchaService reCaptchaService;
		
		private static final int LOCK_DURATION = 1000;
		
		
			@GetMapping("/Login") 
		    public String showLoginPage(Model model) {
				  HashMap<String, String> errors = new HashMap<String,String>();
				    model.addAttribute("errors", errors);
		        return "frontgymlife/member/login";  
		    } 
			
			@PostMapping("/GoogleLogin")
			@ResponseBody
			public Map<String, Object> handleGoogleLogin(@RequestBody Map<String, String> accpwd, HttpSession httpsession) {
			    String googleMail = accpwd.get("googleMail");
			    
			    Map<String, Object> response = new HashMap<>();

			    
			    if (googleMail != null) {
			    	Member googleresult = memberService.checkUserByEmail(googleMail);
			    	if (googleresult != null) {
			            // 這個 Google Email 已經註冊過，可以進行登入操作
			            if (googleresult.isVerified()) {
			                setSessionAttributes(httpsession, googleresult);
			                response.put("status", "success");
			                response.put("redirectUrl", getPageByPermission(googleresult.getUserPermission()));
			                response.put("userPermission", googleresult.getUserPermission());
			            } else {
			                response.put("status", "notActive");
			                response.put("message", "請到信箱開啟驗證連結");
			            }
			           
			        } else {
			        	response.put("status", "notRegistered");
			            response.put("message", "此 Google Email 尚未註冊，請完成註冊流程");
			        }
			    	 return response;
			    }
				return response;
			}
			
			
			
			//登入邏輯
			@PostMapping("/Login")
			@ResponseBody
			public Map<String, Object> handleLogin(@RequestBody Map<String, String> accpwd, HttpSession httpsession) {
			    String userAccount = accpwd.get("userAccount");
			    String userPassword = accpwd.get("userPassword");
			    String googleMail = accpwd.get("googleMail");
			   
			    Map<String, Object> response = new HashMap<>();
			    
			    

			    
			    Optional<Integer> failedAttemptsOpt = Optional.ofNullable((Integer) httpsession.getAttribute("failedAttempts"));
			    Integer failedAttempts = failedAttemptsOpt.orElse(0);

			    if (failedAttempts >= 5 && isAccountLocked(httpsession)) {
			        response.put("status", "fail");
			        response.put("message", "請輸入正確的帳號密碼。");
			        response.put("failedAttempts", failedAttempts);
			        System.out.println("wrong times: " + failedAttempts);
			        return response;
			    }

			    if (StringUtils.isEmpty(userAccount)) {
			        response.put("status", "fail");
			        response.put("message", "請輸入帳號");
			        return response;
			    }

			    if (StringUtils.isEmpty(userPassword)) {
			        response.put("status", "fail");
			        response.put("message", "請輸入密碼");
			        return response;
			    }

			    Member result = memberService.checkLogin(userAccount, userPassword);

			    if (result != null) {
			    	if (result.isVerified()) {
			    		setSessionAttributes(httpsession, result);
			    		response.put("status", "success");
			    		response.put("redirectUrl", getPageByPermission(result.getUserPermission()));
			    		response.put("userPermission", result.getUserPermission());
					}else {
						response.put("status", "notAcctive");
						response.put("message", "請到信箱開啟驗證連結");	
					}
			    } else {
			        failedAttempts++;
			        httpsession.setAttribute("failedAttempts", failedAttempts);
			        System.out.println("Failed login attempts: " + failedAttempts);
			        response.put("status", "fail");
			        response.put("message", "請輸入正確的帳號密碼");
			        response.put("failedAttempts", failedAttempts);
			        if (failedAttempts >= 5) {
			            httpsession.setAttribute("lockTime", System.currentTimeMillis());
			        }
			    }
					
			    return response;
			}

			private boolean isAccountLocked(HttpSession httpsession) {
			    Long lockTime = (Long) httpsession.getAttribute("lockTime");
			    if (lockTime == null) {
			        return false;
			    }
			    return System.currentTimeMillis() - lockTime < LOCK_DURATION;
			}

			private String showLoginPageWithErrors(Model model, HashMap<String, String> errors, Integer failedAttempts) {
			    model.addAttribute("errors", errors);
			    model.addAttribute("failedAttempts", failedAttempts);
			    return "frontgymlife/member/login";
			}

			private void setSessionAttributes(HttpSession httpsession, Member result) {	
			    httpsession.removeAttribute("failedAttempts");
			    httpsession.removeAttribute("lockTime");
			    
			    httpsession.setAttribute("member", result);
			    httpsession.setAttribute("userId", result.getUserId());
			    httpsession.setAttribute("userAccount", result.getUserAccount());
			    httpsession.setAttribute("userName", result.getUserName());
			    httpsession.setAttribute("userGender", result.getUserGender());

			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			    String formattedDate = dateFormat.format(result.getUserBirthDay());
			    httpsession.setAttribute("userBirthDay", formattedDate);

			    httpsession.setAttribute("userAddress", result.getUserAddress());
			    httpsession.setAttribute("userTel", result.getUserTel());
			    httpsession.setAttribute("userEmail", result.getUserEmail());
			    httpsession.setAttribute("userNickName", result.getUserNickName());
			    httpsession.setAttribute("userPermission", result.getUserPermission());
			    httpsession.setAttribute("userStatus", result.getUserStatus());
			    httpsession.setAttribute("userLoggedIn", true);
			    httpsession.setAttribute("userPhoto", result.getUserPhoto());
			    
			   
			    
			    System.out.println("成功獲取帳號: " + result.getUserAccount());
			}
			
			
			
			
			
			//由後端判斷會員的權限來進行跳轉
			private String getPageByPermission(String permission) {
				System.out.println("Permission: " + permission);
				String page = "";
				switch (permission) {
				    case "0":
				        page = "http://localhost:8080/gymlife/";
				        break;
				    case "1":
				        page = "http://localhost:8080/gymlife/Admin";
				        break;
				    default:
				        page = "http://localhost:8080/gymlife/Login";
				}
				System.out.println("Redirecting to page: " + page);
				return page;
			}

			
		  @GetMapping("/logout")
		    public String handleLogout(HttpSession session) {	
		        session.invalidate();
		        return "frontgymlife/member/firstPage";
		    }
		  
		  //忘記密碼page
		  @GetMapping("/loginProblem")
		  public String loginProblem() {
			  return "frontgymlife/member/loginProblem";
		  }
		  ;
		  @PostMapping("/requestPasswordReset")
		  @ResponseBody
		  public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> credentials, HttpSession session) {
			 
			  String userAccount = credentials.get("userAccount");
		      String userEmail = credentials.get("userEmail");
		      
		      session.setAttribute("userAccount", userAccount);
		      
		      
		      Optional<Member> member = memberService.findUserByUserAccountAndUserEmail(userAccount, userEmail);
		      if (member.isPresent()) {
		    	  
		    	  // 生成亂數
		          String verificationCode = String.format("%06d", new Random().nextInt(999999));
		          System.out.println(verificationCode);
		    	  
		         // 存取至session,並且限制15分鐘
		    	  session.setAttribute("verificationCode", verificationCode);
		          session.setAttribute("codeExpiration", LocalDateTime.now().plusMinutes(1)); 
		    	 
		              // call your mail sending service here
		          mailService.prepareAndSend(userEmail, "Your verification code", verificationCode);

		            return new ResponseEntity<>("驗證碼已寄出", HttpStatus.OK);
		        } else {
		            return new ResponseEntity<>("找不到該用戶還有帳號", HttpStatus.BAD_REQUEST);
		        }
		  }
		
		  //驗證 驗證碼是否正確
		  @PostMapping("/VerifyCode")
		  @ResponseBody
		  public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> data, HttpSession session) {
		      String code = data.get("verificationCode");

		      // Get verification code and its expiration from session.
		      String verificationCode = (String) session.getAttribute("verificationCode");
		      LocalDateTime codeExpiration = (LocalDateTime) session.getAttribute("codeExpiration");

		      // Check if the code matches and it's not expired.
		      if (code.equals(verificationCode) &&
		          LocalDateTime.now().isBefore(codeExpiration)) {
		          // If the code is correct and not expired, allow user to change password.
		          return new ResponseEntity<>("成功", HttpStatus.OK);
		      } else {
		          return new ResponseEntity<>("失敗", HttpStatus.BAD_REQUEST);
		      }
		  }
		  
		  @PostMapping("/ResetPassword")
		  @ResponseBody
		  public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> data, HttpSession session) {
		      String newPassword = data.get("newPassword");
		      String confirmPassword = data.get("confirmPassword");

		      if (!newPassword.equals(confirmPassword)) {
		          return new ResponseEntity<>("Passwords do not match.", HttpStatus.BAD_REQUEST);
		      }

		     
		      String userAccount = (String) session.getAttribute("userAccount");
		      
		      // 使用用户名来查找用户
		      Optional<Member> optionalMember = memberService.findUserByUserAccount(userAccount);

		     

		      if (!optionalMember.isPresent()) {
		          return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
		      }

		      // Update the password
		      Member member = optionalMember.get();
		      member.setUserPassword(pwdEncoder.encode(newPassword));

		      memberService.seveUserPassword(member);
		      
		      System.out.println("setPassword down");
		      session.removeAttribute("userAccount");
		      
		      return new ResponseEntity<>("Password updated successfully.", HttpStatus.OK);
		  }
		  
}
