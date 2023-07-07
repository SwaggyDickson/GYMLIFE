package tw.gymlife.activity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	        // 如果存在session跳轉到報名頁面
	        return "frontgymlife/activity/Registration";
	    	}
	    }
	    
	    // 不存在session，使用SweetAlert彈出提示框後跳轉到登入頁面
	    String alertMessage = "请先登录/注册会员";
	    return "redirect:/Login?alert=" + URLEncoder.encode(alertMessage, StandardCharsets.UTF_8);
	}

	// 創建報名記錄
	@PostMapping
	public ResponseEntity<Registration> createRegistration(@RequestParam("userId") Integer userId,
			@RequestBody Registration registration) {
		// 根據會員ID查詢會員
		Member member = mService.getMemberById(userId);
		if (member == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// 設定報名記錄的會員ID
		registration.setUserId(member.getUserId());

		// 創建報名記錄
		Registration createdRegistration = rService.insertRegistration(registration);
		return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
	}

	// 根據報名ID獲取報名記錄
	@GetMapping("/{registrationId}")
	public ResponseEntity<Registration> getRegistrationById(@PathVariable int registrationId) {
		Registration registration = rService.getRegistrationById(registrationId);
		if (registration != null) {
			return new ResponseEntity<>(registration, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// 根據活動ID獲取報名記錄列表
	@GetMapping("/activity/{activityId}")
	public ResponseEntity<List<Registration>> getRegistrationsByActivityId(@PathVariable int activityId) {
		List<Registration> registrations = rService.getRegistrationsByActivityId(activityId);
		return new ResponseEntity<>(registrations, HttpStatus.OK);
	}

	// 根據會員ID獲取報名記錄列表
	@GetMapping("/member/{userId}")
	public ResponseEntity<List<Registration>> getRegistrationsByMemberId(@PathVariable int userId) {
		List<Registration> registrations = rService.getRegistrationsByMemberId(userId);
		return new ResponseEntity<>(registrations, HttpStatus.OK);
	}

	// 更新報名記錄
	@PutMapping("/{registrationId}")
	public ResponseEntity<Registration> updateRegistration(@PathVariable int registrationId,
			@RequestParam("status") String registrationStatus, @RequestParam("date") Date registrationDate) {
		Registration updatedRegistration = rService.updateRegistrationById(registrationId, registrationStatus,
				registrationDate);
		if (updatedRegistration != null) {
			return new ResponseEntity<>(updatedRegistration, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// 刪除報名記錄
	@DeleteMapping("/{registrationId}")
	public ResponseEntity<Void> deleteRegistration(@PathVariable int registrationId) {
		rService.deleteRegistration(registrationId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}