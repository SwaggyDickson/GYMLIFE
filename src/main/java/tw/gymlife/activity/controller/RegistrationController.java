package tw.gymlife.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.model.Registration;
import tw.gymlife.activity.service.ActivityService;
import tw.gymlife.activity.service.RegistrationService;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MemberService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Controller
public class RegistrationController {

	@Autowired
	public RegistrationService rService;

	@Autowired
	public MemberService mService;

	@Autowired
	public ActivityService aService;

	@GetMapping("/goRegistration")
	public String showRegistrationForm(@RequestParam("activityId") Integer activityId, HttpSession httpsession, Model m) {
	    // 判斷是否存在 session
	    Integer userId = (Integer) httpsession.getAttribute("userId");
	    
	    // 根據會員ID獲取會員資訊
	    if (userId!=null) {
	    	Member member = mService.getMemberById(userId);
	        // 根據活動ID取得活動資訊
	        Activity activity = aService.getActivityById(activityId);
	        if (activity != null) {
	        m.addAttribute("activity", activity);
	        m.addAttribute("member", member);
	        
	        // 計算倒計時時間差 報名截止日 & 活動日期倒數計時器
	        Date now = new Date();
	        Date registrationEndDate = activity.getRegistrationEndDate();
	        Date activityDate = activity.getActivityDate();
	        long remainingMillisecondsEndDate = registrationEndDate.getTime() - now.getTime();
	        long remainingSecondsEndDate = remainingMillisecondsEndDate / 1000;
	        long remainingMillisecondsDate = activityDate.getTime() - now.getTime();
	        long remainingSecondsDate = remainingMillisecondsDate / 1000;
	        

	        // 將倒數計時器時間差傳給前端
	        m.addAttribute("remainingSecondsEndDate", remainingSecondsEndDate);
	        m.addAttribute("remainingSecondsDate", remainingSecondsDate);
	        

	        // 如果存在session跳轉到報名頁面
	        return "frontgymlife/activity/Registration";
	    	}
	    }
	    
	    // 不存在session，使用SweetAlert彈出提示框後跳轉到登入頁面
	    String alertMessage = "請先登入或註冊會員";
	    return "redirect:/Login?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	}


	@PostMapping("/submitRegistration")
	public String submitRegistrationForm(@ModelAttribute Registration registration, @RequestParam("activityId") Integer activityId, HttpSession httpsession, Model m) {
	    // 判斷是否存在 session
	    Integer userId = (Integer) httpsession.getAttribute("userId");

	    // 根據會員ID獲取會員資訊
	    Member member = mService.getMemberById(userId);

	    // 根據活動ID取得活動資訊
	    Activity activity = aService.getActivityById(activityId);

	    // 檢查會員和活動是否存在，以及其他驗證邏輯...
	    if (member != null && activity != null) {
	    	// 检查会员是否已经报名过该活动
	        boolean isRegistered = rService.isMemberRegisteredForActivity(userId, activityId);
	        if (isRegistered) {
	            // 會員已經報名過該活動，跳轉到報名失敗頁面或其他適當的處理...使用SweetAlert弹出提示框后跳转到登录页面
	            String alertMessage = "您已經報名過該活動，不允許重複報名";
	            return "redirect:/activityHome?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	        }

	    	
	        // 檢查報名人數是否已滿
	        if (activity.getActivityApplicantsQty() < activity.getApplicantLimitedQty()) {
	            // 創建報名紀錄並保存到資料庫...
	            Registration newRegistration = new Registration();
	            newRegistration.setMember(member);
	            newRegistration.setActivity(activity);
	            newRegistration.setRegistrationStatus("報名成功");
	            newRegistration.setRegistrationDate(new Date());
	            newRegistration.setEmergencyContact(registration.getEmergencyContact());
	            newRegistration.setEmergencyContactPhone(registration.getEmergencyContactPhone());
	            newRegistration.setRelationship(registration.getRelationship());
	            
	            // 儲存報名紀錄到資料庫...
	            rService.insertRegistration(newRegistration);

	            // 更新活動報名人數
	            activity.setActivityApplicantsQty(activity.getActivityApplicantsQty() + 1);
	            aService.insertActivity(activity);
	            
	            // 傳遞報名紀錄到前端
	            m.addAttribute("registration", newRegistration);
	            m.addAttribute("activity", activity);

	            // 跳轉到報名成功頁面或其他適當的處理...
	            String alertMessage = "您已成功報名！";
	            return "redirect:/activityDetails?activityId=" + activityId + "&alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	            
	        } else {
	            // 報名人數已滿，跳轉到報名失敗頁面或其他適當的處理...使用SweetAlert彈出提示框後跳轉到登入頁面
	    	    String alertMessage = "報名人數已滿，請參考其他活動";
	    	    return "redirect:/activityHome?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	        }
	    } else {
	        // 無法報名，跳轉到報名失敗頁面或其他適當的處理...使用SweetAlert彈出提示框後跳轉到登入頁面
    	    String alertMessage = "無法報名，請再試一次";
    	    return "redirect:/activityHome?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	    }
		
	}
	
	//跳轉報名紀錄頁面
	@GetMapping("/goRegistrationRecord")
	public String goRegistrationRecord(HttpSession httpsession, Model m) {
	    // 判斷是否存在 session
	    Integer userId = (Integer) httpsession.getAttribute("userId");

	    // 如果不是會員則跳轉
	    if (userId == null) {
	        String alertMessage = "請先登入或註冊會員";
	        return "redirect:/Login?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	    }

	    // 根據會員ID獲取報名記錄列表
	    List<Registration> registrations = rService.getRegistrationsByMemberId(userId);

	    // 獲取每個報名記錄對應的活動資訊
	    List<Activity> allactivity = new ArrayList<>();
	    for (Registration registration : registrations) {
	        Activity activity = aService.getActivityById(registration.getActivityId());
	        allactivity.add(activity);
	    }
	    
	    // 檢查報名紀錄是否為空
	    if (userId!=null && registrations.isEmpty()) {
	    	// 該會員目前無報名紀錄，使用SweetAlert彈出提示框後跳轉到所有活動頁面
    	    String alertMessage = "目前尚無報名紀錄";
    	    return "redirect:/activityHome?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	    }

	    m.addAttribute("registrations", registrations);
	    m.addAttribute("allactivity", allactivity);
	    return  "frontgymlife/activity/RegistrationRecord"; 
	}
	
	// 後台-該活動有報名紀錄的會員
	@GetMapping("/goBackRegistrationRecord") 
	public String getActivityDetails(@RequestParam("activityId") Integer activityId, Model m) {
	    // 根據活動ID獲取活動資訊
	    Activity activity = aService.getActivityById(activityId);

	    // 根據活動ID獲取會員報名記錄列表
	    List<Registration> registrations = rService.getRegistrationsByActivityId(activityId);

	    // 獲取會員資訊
	    List<Member> members = new ArrayList<>();
	    for (Registration registration : registrations) {
	        Integer userId = registration.getUserId();
	        Member member = mService.getMemberById(userId);
	        members.add(member);
	    }

	    m.addAttribute("activity", activity);
	    m.addAttribute("registrations", registrations);
	    m.addAttribute("members", members);
	    
	    // 檢查報名紀錄是否為空
	    if (registrations.isEmpty()) {
	    	// 該活動目前無報名紀錄，使用SweetAlert彈出提示框後跳轉到登入頁面
    	    String alertMessage = "該活動目前無報名紀錄";
    	    return "redirect:/activity/getAll?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	    }

	  return "backgymlife/activity/backRegistrationRecord"; 
	}

	
}