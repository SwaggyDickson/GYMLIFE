package tw.gymlife.activity.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.model.NominatimResult;
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

	// 單筆活動詳細資料
	@GetMapping("/activityDetails")
	public String getDetails(@RequestParam("activityId") Integer activityId, Model m) {
	    // 根據活動ID找到該筆資料
	    Activity activity = aService.getActivityById(activityId);
	    
	    // 获取活动地点
	    String activityLocation = activity.getActivityLocation();

	    
	    // 獲取活動的報名截止日期和當前日期進行比較
	    Date registrationEndDate = activity.getRegistrationEndDate();
	    Date currentDate = new Date();

	    // 比較當前日期和活動報名截止日期
	    if (currentDate.before(registrationEndDate)) {
	        // 當前日期在報名截止日期之前，顯示"我要報名"
	        m.addAttribute("buttonText", "我要報名");
	    } else {
	        // 當前日期在報名截止日期之後，顯示"報名截止"
	        m.addAttribute("buttonText", "報名已截止");
	    }
	    
	    // 利用model傳給前端
	    m.addAttribute("activity", activity);
	    // 将活动地点添加到模型中
	    m.addAttribute("activityLocation", activityLocation);
	    
        // 計算倒計時時間差 報名截止日 & 活動日期倒數計時器
        Date now = new Date();
        Date activityDate = activity.getActivityDate();
        long remainingMillisecondsEndDate = registrationEndDate.getTime() - now.getTime();
        long remainingSecondsEndDate = remainingMillisecondsEndDate / 1000;
        long remainingMillisecondsDate = activityDate.getTime() - now.getTime();
        long remainingSecondsDate = remainingMillisecondsDate / 1000;
        

        // 將倒數計時器時間差傳給前端
        m.addAttribute("remainingSecondsEndDate", remainingSecondsEndDate);
        m.addAttribute("remainingSecondsDate", remainingSecondsDate);
	    return "frontgymlife/activity/Details";
	}
	
	// AJAX API：获取活动地点的经纬度
    @GetMapping("/getActivityLocation")
    public String getActivityLocation(@RequestParam("activityId") Integer activityId, Model m) {
        // 根据活动ID找到该笔数据
        Activity activity = aService.getActivityById(activityId);

        // 获取活动地点
        String activityLocation = activity.getActivityLocation();

        // 使用Nominatim进行Geocoding，获取活动地点的经纬度
        String nominatimUrl = "https://nominatim.openstreetmap.org/search?format=json&q=";
        RestTemplate restTemplate = new RestTemplate();
        String url = nominatimUrl + activityLocation;
        NominatimResult[] results = restTemplate.getForObject(url, NominatimResult[].class);

        if (results != null && results.length > 0) {
            // 获取第一个结果的经纬度
            double activityLatitude = results[0].getLat();
            double activityLongitude = results[0].getLon();

            // 将活动地点的经纬度添加到Model对象中
            m.addAttribute("activityLatitude", activityLatitude);
            m.addAttribute("activityLongitude", activityLongitude);
        } else {
            // 处理找不到结果的情况
            // ...
        }

        return "activityLocation";
    }
}

