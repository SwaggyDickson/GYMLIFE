package tw.gymlife.member.controller;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.messaging.simp.SimpMessagingTemplate;



import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MailService;
import tw.gymlife.member.service.MemberService;



@Controller
public class Register {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@GetMapping("/Register")
	 public String showRegisterPage(Model model) {
	        return "frontgymlife/member/register";
	    }	
	
	@GetMapping("/RegisterForGoogle")
	 public String showRegisterForGoogle(Model model) {
	        return "frontgymlife/member/registerForGoogle";
	    }
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
				} catch (ParseException e) {
					setValue(null);
				}
			}
			public String getAsText() {
				return new SimpleDateFormat("yyyy-MM-dd").format((Date) getValue());
			}        
		});
	}
	
	@PostMapping("/Register")
    public String handleRegistration(@Valid  @ModelAttribute("member") Member member,BindingResult bindingResult,
    		@RequestParam("userAccount") String userAccount,
    		@RequestParam("userAddressFirst") String userAddressFirst, 
    		@RequestParam("userAddressDetail") String userAddressDetail,
    		Model model,RedirectAttributes redirectAttributes,
    		HttpSession session) {
		
		   // 伺服器端驗證：帳號長度檢查
	    if (userAccount.length() < 8 || userAccount.length() > 20) {
	       // model.addAttribute("errorMsg", "帳號必須在8到20個英數字之內");
	        return "frontgymlife/member/register";
	    }

	    // 伺服器端驗證：帳號只能包含數字和字母檢查
	    if (!userAccount.matches("^[a-zA-Z0-9]*$")) {
	       // model.addAttribute("errorMsg", "帳號只能使用英文數字及字母");
	        return "frontgymlife/member/register";
	    }
		if (bindingResult.hasErrors()) {
			//redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getFieldError().getDefaultMessage());
			return "frontgymlife/member/register";
	    }
		
        boolean isExist = memberService.checkIfUserAccountExist(userAccount);
		
        if (isExist) {
			//model.addAttribute("errorMsg", "此帳號已存在");
			return "frontgymlife/member/register";
		}else {
			
		//合併字串
		String userAddress = userAddressFirst + userAddressDetail;
        member.setUserAddress(userAddress);
        
        java.util.Date registerDateUtil = new java.util.Date();
        java.sql.Date registerDateSql = new java.sql.Date(registerDateUtil.getTime());
        member.setUserRegisterDate(registerDateSql);
        
        //設置預設照片
        try {
            Path defaultImagePath = Paths.get("src/main/resources/static/gym/member/image/default.png");
            byte[] defaultImageBytes = Files.readAllBytes(defaultImagePath);
            
            member.setUserPhoto(defaultImageBytes);
        } catch (IOException e) {
            
        }

        Member insertedMember = memberService.register(member);

        String subject = "Welcome to GymLife!";
        String message = "您好!! " + member.getUserName() + " 歡迎您加入我們的GymLife的大家庭，請好好享受我們提供的服務";
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        System.out.println("getUUid: "+ uuid);
        session.setAttribute("token", uuid);
        message += "http://localhost:8080/gymlife/checkEmailStatus?token="+uuid+"&no="+member.getUserId();
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        final String finalMessage = message;
        executorService.submit(() -> {
            // call your mail sending service here
        	CompletableFuture<Boolean> future = mailService.prepareAndSend(member.getUserEmail(), subject, finalMessage);
        	boolean isMailSent;
        	try {
        	    isMailSent = future.get();  // wait for the Future to complete
        	} catch (InterruptedException | ExecutionException e) {
        	    e.printStackTrace();
        	    isMailSent = false;
        	}
        	if (isMailSent) {
        	    System.out.println("信件送出");
        	} else {    
        	    System.out.println("沒有送出");
        	}
             
        });
      
        if (insertedMember != null) {
            // Registration succeeded, redirect to a success page List<Member> allMembers = memberService.getAllMembers();
            
        	List<Member> allMembers = memberService.selectAllMembers();
//        	List<Member> newMembers = new ArrayList<>();
//            newMembers.add(insertedMember);
        	StompHeaderAccessor headerAccessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
        	headerAccessor.setDestination("/topic/MemberQuery");
        	headerAccessor.setMessage(userAddressDetail); // 设置用户信息
        	headerAccessor.setHeader("username", insertedMember.getUserName()); // 设置用户名信息
        	

        	template.convertAndSend(headerAccessor.getDestination(), allMembers);
           
            return "frontgymlife/member/afterRegister";
        } else {
            return "member/RegisterFail";
        }
    }
}
	@GetMapping("/checkUserAccount")
	@ResponseBody
	public Map<String, Boolean> checkUserAccount(@RequestParam("userAccount") String userAccount) {
	    
	    boolean isExist = memberService.checkIfUserAccountExist(userAccount);

	    Map<String, Boolean> response = new HashMap<>();
	    response.put("exists", isExist);
	    return response;
	}
	
	@GetMapping("/checkEmailStatus")
	public String handleEmailVerification(@RequestParam("token") String token, @RequestParam("no") int userId, HttpSession session) {
	    // 从Session或者数据库中获取UUID
	    String storedToken = (String) session.getAttribute("token");
	    
	    if (storedToken != null && storedToken.equals(token)) {
	        // UUID匹配，进行用户验证
	        // 这里调用你的service层方法来更新用户的验证状态
	    	 memberService.verifyUser(userId);
	    	 return "frontgymlife/member/firstPage"; 
	    } else {
	        // UUID不匹配，返回错误信息
	    	return "frontgymlife/member/firstPage"; 
	    }
	}
	



	
}
