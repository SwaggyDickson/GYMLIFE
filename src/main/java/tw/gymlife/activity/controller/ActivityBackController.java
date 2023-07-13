package tw.gymlife.activity.controller;

import java.io.IOException;
import tw.gymlife.activity.model.Registration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import tw.gymlife.activity.dao.ActivityImageRepository;
import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.model.ActivityImage;
import tw.gymlife.activity.service.ActivityService;
import tw.gymlife.activity.service.RegistrationService;

@Controller
public class ActivityBackController {

	@Autowired
	private ActivityService aService;
	
	@Autowired
	private ActivityImageRepository imageRepo;
	
	@Autowired
	public RegistrationService rService;

	// 顯示新增活動頁面
	@GetMapping("/activity/insert")
	public String showInsertActivityPage(Model model) {
		model.addAttribute("activity", new Activity());
		return "backgymlife/activity/InsertActivity";
	}
	
	//新增活動
	@PostMapping("/activity/insert")
	public String insertActivity(@RequestParam("activityName") String activityName,
	        @RequestParam("activityDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date activityDate, @RequestParam("activityLocation") String activityLocation,
	        @RequestParam("activityCategory") String activityCategory,
	        @RequestParam("activityCover") MultipartFile activityCover,
	        @RequestParam("registrationStartDate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date registrationStartDate,
	        @RequestParam("registrationEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date registrationEndDate, @RequestParam("organizer") String organizer,
	        @RequestParam("activityStatus") String activityStatus,
	        @RequestParam("activityInfo") String activityInfo,
	        @RequestParam("applicantLimitedQty") Integer applicantLimitedQty,
	        @RequestParam("activityApplicantsQty") Integer activityApplicantsQty, Model m) throws IOException {

	    // 建立新的活動物件並設定屬性
	    Activity activity = new Activity();
	    activity.setActivityName(activityName);
	    activity.setActivityDate(activityDate);
	    activity.setActivityLocation(activityLocation);
	    activity.setActivityCategory(activityCategory);
	    activity.setActivityCover(activityCover.getBytes());
	    activity.setRegistrationStartDate(registrationStartDate);
	    activity.setRegistrationEndDate(registrationEndDate);
	    activity.setOrganizer(organizer);
	    activity.setActivityStatus(activityStatus);
	    activity.setActivityInfo(activityInfo);
	    activity.setApplicantLimitedQty(applicantLimitedQty);
	    activity.setActivityApplicantsQty(activityApplicantsQty);

	    // 將活動物件保存到數據庫
	    aService.insertActivity(activity);

	    return "redirect:/activity/getAll"; 
	}
	
	

	// 顯示封面圖片
	@GetMapping("/dowloadImageActivity/{activityId}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer activityId) {
		Activity img = aService.getActivityById(activityId);
		byte[] activityCover = img.getActivityCover();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);

		return new ResponseEntity<byte[]>(activityCover, headers, HttpStatus.OK);

	}
	
	// 查詢所有活動
	@GetMapping("/activity/getAll")
	public String getAllActivity(Model m) {
		List<Activity> allActivity = aService.getAllActivity();
		m.addAttribute("allActivity", allActivity);
		return "backgymlife/activity/GetAllActivity";
	}

	// 刪除活動
	@Transactional
	@DeleteMapping("/activity/delete")
	public String deletePost(@RequestParam("activityId") Integer activityId) {
		aService.deleteActivityByid(activityId);
		return "redirect:/activity/getAll";
	}
	
	// ajax查詢所有
    @GetMapping("/activity/api/getAll")
    public List<Activity> getAllActivity() {
        List<Activity> allActivity = aService.getAllActivity();
        return allActivity;
    }
	
//	// ajax刪除
//	@ResponseBody
//	@DeleteMapping("/activity/api/delete")
//	public ResponseEntity<String> deletePost(@RequestParam("activityId") Integer activityId) {
//	    aService.deleteActivityByid(activityId);
//	    List<Activity> allActivity = aService.getAllActivity(); // 获取更新后的活动列表数据
//	    return ResponseEntity.ok("Activity deleted successfully."); // 返回删除成功的消息
//	}
//	
	// 進入更新頁面時獲取活動資訊及活動類型
	@GetMapping("/activity/update")
	public String showUpdateForm(@RequestParam("activityId") Integer activityId, Model m) {
		Activity activity = aService.getActivityById(activityId);
		m.addAttribute("activity", activity);

		return "backgymlife/activity/GetActivity";
	}

	// 更新活動
	@Transactional
	@PostMapping("/activity/update")
	public String updateActivity(@RequestParam("activityId") Integer activityId,
			@RequestParam("activityName") String activityName, @RequestParam("activityDate")@DateTimeFormat(pattern = "yyyy-MM-dd")  Date activityDate,
			@RequestParam("activityLocation") String activityLocation,
			@RequestParam("activityCategory") String activityCategory,
			@RequestParam("activityCover") MultipartFile activityCover,
			@RequestParam("registrationStartDate")@DateTimeFormat(pattern = "yyyy-MM-dd")  Date registrationStartDate,
			@RequestParam("registrationEndDate")@DateTimeFormat(pattern = "yyyy-MM-dd")  Date registrationEndDate, @RequestParam("organizer") String organizer,
			@RequestParam("activityStatus") String activityStatus,
			@RequestParam("activityInfo") String activityInfo,
			@RequestParam("applicantLimitedQty") Integer applicantLimitedQty,
			@RequestParam("activityApplicantsQty") Integer activityApplicantsQty, Model m) {

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
		Activity updateActivityById = aService.updateActivityById(activityId, activityName, activityDate,
				activityLocation, activityCategory, activityCoverBytes, registrationStartDate, registrationEndDate,
				organizer,activityStatus,activityInfo, applicantLimitedQty, activityApplicantsQty);

		// 判斷更新是否成功
		if (updateActivityById != null) {
			// 更新成功后，調用dowloadImage方法獲取活動封面圖片，並保存到Model中
			ResponseEntity<byte[]> imageResponse = downloadImage(activityId);
			byte[] activityCoverData = imageResponse.getBody();
			// 将活動封面圖片儲存在Model中，供視圖使用
			m.addAttribute("activityCover", activityCoverData);

			return "redirect:/activity/getAll";
		} else {
			// 處理更新失敗的情況
			return "error-page";
		}
	}
	
	//處理CKEditor插入圖片 - 上傳圖片進資料庫並取得URL
	@PostMapping("/activity/api/upload")
	public ResponseEntity<?> ckEditorUploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		// 建立一個新的 ActivityImage 物件
		ActivityImage image = new ActivityImage();
		
		// 將上傳的檔案的位元組陣列設定到 ActivityImage 物件的 photoFile 屬性
		image.setPhotoFile(file.getBytes());
		
	    // 將 ActivityImage 物件保存到資料庫中並獲取保存後的 imageId
		ActivityImage reImage = imageRepo.save(image);

		// 構建圖片的 URL
		String imageUrl = "http://localhost:8080/gymlife/activity/api/imgdownload/" + reImage.getImageId();

		// 建立一個 Map 物件，將圖片的 URL 存入其中
		Map<String, String> urlMap = new HashMap<String, String>();
		urlMap.put("url", imageUrl);

		// 使用 ResponseEntity 返回包含 URL 的 Map，並設定狀態碼為 HttpStatus.OK
		return new ResponseEntity<Map<String, String>>(urlMap, HttpStatus.OK);
	}
	
	//處理CKEditor插入圖片- 下載上傳的圖片的後端API
	@ResponseBody
	@GetMapping("/activity/api/imgdownload/{imageId}")
	public ResponseEntity<byte[]> downloadCKEditorImage(@PathVariable Integer imageId) {

		// 根據 imageId 從資料庫中查詢對應的 ActivityImage
		Optional<ActivityImage> option = imageRepo.findById(imageId);

		if (option.isPresent()) {
			// 如果找到了對應的 ActivityImage
			ActivityImage photo = option.get();
			byte[] photoFile = photo.getPhotoFile();
			// 返回圖片檔案的 Response，設定檔案類型為 IMAGE_JPEG
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(photoFile);
		}
		// 如果找不到對應的 ActivityImage，返回 null
		return null;

	}


//	// ajax查詢所有
//    @GetMapping("/activity/api/getAll")
//    public List<Activity> getAllActivity() {
//        List<Activity> getallActivity = aService.getAllActivity();
//        return getallActivity;
//    }
	
	// ajax刪除
	@ResponseBody
	@DeleteMapping("/activity/api/delete")
	public List<Activity> deleteAjax(@RequestParam("activityId") Integer activityId, Model m){
		aService.deleteActivityByid(activityId);
		return getAllActivity();
	}

	



}
