package tw.gymlife.activity.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tw.gymlife.activity.model.Registration;

import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.service.ActivityService;
import tw.gymlife.activity.service.RegistrationService;

@Controller
public class ActivityFrontController {

	@Autowired
	private ActivityService aService;

	@Autowired
	public RegistrationService rService;

	// 查詢所有活動
	@GetMapping("/activityHome")
	public String getAllActivity(Model m) {
	    List<Activity> allActivity = aService.getAllActivity();

	    m.addAttribute("allActivity", allActivity);
	    return "frontgymlife/activity/ActivityHome";
	}

	@GetMapping("/activityDetails")
	public String getDetails(@RequestParam("activityId") Integer activityId, Model m) {
	    // 根據活動ID找到該筆資料
	    Activity activity = aService.getActivityById(activityId);

	    // 利用model傳給前端
	    m.addAttribute("activity", activity);
	    return "frontgymlife/activity/Details";
	}
	


}
