package tw.gymlife.activity.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.service.ActivityService;

@Controller
public class ActivityController {
	
	@Autowired
	private ActivityService aService;
	
	@GetMapping("activity/showAll")
	public String showActivity(Model model) {
		List<Activity> list = aService.listActivity();
		model.addAttribute("activityList", list);
		return "backgymlife/activity/GetAllActivity";
	}
	
	@GetMapping("/dowloadImage/{activityId}")
	public ResponseEntity<byte[]> dowloadImage(@PathVariable Integer activityId){
		Activity img = aService.findById(activityId);
		byte[] activityCover = img.getActivityCover();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		return new ResponseEntity<byte[]>(activityCover, headers, HttpStatus.OK);
		
	}
	
    @GetMapping("/activity/add")
    public String showAddActivityForm(Model model) {
        model.addAttribute("activity", new Activity());
        return "backgymlife/activity/InsertActivity";
    }

    @PostMapping("/activity/add")
    public String addActivity(
    		@RequestParam("activityName") String activityName,
    		@RequestParam("activityDate") Date activityDate,
    		@RequestParam("activityLocation") String activityLocation,
    		@RequestParam("activityStatus") String activityStatus,
    		@RequestParam("activityCover") MultipartFile activityCover,
    		@RequestParam("activityContent") String activityContent,Model model) {
    	
    	try {
    		
    	Activity activity = new Activity();
    	activity.setActivityName(activityName);
    	activity.setActivityDate(activityDate);
    	activity.setActivityLocation(activityLocation);
    	activity.setActivityStatus(activityStatus);
		activity.setActivityCover(activityCover.getBytes());
		activity.setActivityContent(activityContent);
		
		aService.insertActivity(activity);
		
		List<Activity> list = aService.listActivity();
		model.addAttribute("activityList", list);
		
		return "backgymlife/activity/GetAllActivity";
		
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/activity/showAll";
		}
    }
    
    	@GetMapping("/activity/update")
    	public String updatePage(@RequestParam("activityId") Integer activityId,Model model) {
    		Activity activity = aService.findById(activityId);
    		model.addAttribute("activity", activity);
    		
    		return "backgymlife/activity/GetActivity";
    	}
        
        @PostMapping("/activity/update")
        public String updateActivity(@RequestParam("activityId") Integer activityId,
                                     @RequestParam("activityName") String activityName,
                                     @RequestParam("activityDate") Date activityDate,
                                     @RequestParam("activityLocation") String activityLocation,
                                     @RequestParam("activityStatus") String activityStatus,
                                     @RequestParam(value = "activityCover", required = false) MultipartFile activityCover,
                                     @RequestParam("activityContent") String activityContent,
                                     Model model) {

        	 // 檢查活動封面圖片是否存在並且不為空值
            byte[] activityCoverBytes = null;
            if (activityCover != null && !activityCover.isEmpty()) {
                try {
                	// 讀取封面圖片的字節數組
                    activityCoverBytes = activityCover.getBytes();
                } catch (IOException e) {
                	// 處理讀封面圖片異常的情況，返回錯誤頁面
                    return "error-page";
                }
            }

            // 更新活動訊息
            Activity updatedActivity = aService.updateActivityById(activityId, activityName, activityDate,
                    activityLocation, activityStatus, activityCoverBytes, activityContent);

            // 判斷更新是否成功
            if (updatedActivity != null) {
                // 更新成功后，調用dowloadImage方法獲取活動封面圖片，並保存到Model中
                ResponseEntity<byte[]> imageResponse = dowloadImage(activityId);
                byte[] activityCoverData = imageResponse.getBody();
                // 将活動封面圖片儲存在Model中，供視圖使用
                model.addAttribute("activityCover", activityCoverData);

                // 獲取更新后的活動列表
                List<Activity> list = aService.listActivity();
        		model.addAttribute("activityList", list);
        		
        		// 返回顯示所有活動的頁面
        		return "backgymlife/activity/GetAllActivity";
            } else {
                // 處理更新失敗的情況
                return "error-page";
            }
        }
        

   
    	
    	@DeleteMapping("/activity/delete")
    	public String deletePost(@RequestParam("activityId") Integer activityId) {
    		aService.deleteByid(activityId);
    		return "redirect:/activity/showAll";
    	}
    }
    	

	
