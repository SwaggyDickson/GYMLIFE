package tw.gymlife.member.controller;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import tw.gymlife.member.service.MemberService;

	@Controller
	public class AuthController {
			
		@Autowired 
		private MemberService memberService;
		
		private static final int LOCK_DURATION = 1000;
		
		//跳轉登入頁面
			@GetMapping("/Login") 
		    public String showLoginPage(Model model) {
				  HashMap<String, String> errors = new HashMap<String,String>();
				    model.addAttribute("errors", errors);
		        return "frontgymlife/member/login";  
		    } 
			@PostMapping("/Login")
			@ResponseBody
			public Map<String, Object> handleLogin(@RequestBody Map<String, String> accpwd, HttpSession httpsession) {
			    String userAccount = accpwd.get("userAccount");
			    String userPassword = accpwd.get("userPassword");
			    
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
		  
		  public String testGit() {
			  return "請接收數據";
		  }
		  
}
