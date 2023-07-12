package tw.gymlife.activity.service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.activity.dao.ActivityRepository;

import tw.gymlife.activity.model.Activity;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository aRepo;
	
	// 活動主檔CRUD

	// 新增
	public Activity insertActivity(Activity activity) {
		Date currentDate = new Date();
		activity.setCreateTime(currentDate);
		return aRepo.save(activity);
	}

	// 查單筆(id)
	public Activity getActivityById(Integer activityId) {
		Optional<Activity> optional = aRepo.findById(activityId);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 刪除(id)
	public void deleteActivityByid(Integer activityId) {
		aRepo.deleteById(activityId);
	}

	// 查所有活動
	public List<Activity> getAllActivity() {
		return aRepo.findAll();
	}

	// 更新
	@Transactional
	public Activity updateActivityById(Integer activityId, String activityName, Date activityDate,
			String activityLocation,String activitCategory,byte[] activityCover, Date registrationStartDate, Date registrationEndDate,
			String organizer,String activityStatus,String activityInfo,Integer applicantLimitedQty,Integer activityApplicantsQty) {
		Optional<Activity> optional = aRepo.findById(activityId);
		if (optional.isPresent()) {
			Activity activity = optional.get();
			activity.setActivityName(activityName);
			activity.setActivityDate(activityDate);
			activity.setActivityLocation(activityLocation);
			activity.setActivityCategory(activitCategory);
			activity.setActivityCover(activityCover);
			activity.setRegistrationStartDate(registrationStartDate);
			activity.setRegistrationEndDate(registrationEndDate);
			activity.setOrganizer(organizer);
			activity.setActivityStatus(activityStatus);
			activity.setActivityInfo(activityInfo);
			activity.setApplicantLimitedQty(applicantLimitedQty);
			activity.setActivityApplicantsQty(activityApplicantsQty);

			Date currentDate = new Date();
			activity.setUpdateTime(currentDate);

			return aRepo.save(activity);
		} else {
			System.out.println("No update data");
			return null;
		}

	}
	
//	// 根据活動狀態排序
//    public List<Activity> getActivitiesByStatus() {
//        return aRepo.findAllByOrderByActivityStatusAsc();
//    }
//    
//    // 根据活動日期的年份查詢
//    public List<Activity> getActivitiesByYear(String year) {
//        return aRepo.findAllByActivityDateStartingWith(year);
//    }
//
//    // 根据活動日期的月份查詢
//    public List<Activity> getActivitiesByMonth(String month) {
//        return aRepo.findAllByActivityDateContaining(month);
//    }
//
//    // 模糊查询
//    public List<Activity> searchActivitiesByKeyword(String keyword) {
//        return aRepo.searchActivitiesByKeyword(keyword);
//    }
	
//	//根據類別找活動
//	public List<Activity> getActivitiesByCategory(String activityCategory) {
//	    List<Activity> activitiesByCategory = new ArrayList<>();
//	    List<Activity> allActivity = getAllActivity(); // 获取所有活动
//
//	    for (Activity activity : allActivity) {
//	        if (activity.getActivityCategory().equals(activityCategory)) {
//	            activitiesByCategory.add(activity);
//	        }
//	    }
//
//	    return activitiesByCategory;
//	}
//	
//	// 使用者可自行新增活動類別的方法
//    public void addCategory(String activityCategory) {
//        // 在此處將自訂的活動類別添加到activity表格的activityCategory欄位
//        // 您可以使用activityRepository或其他資料庫存取層進行操作
//        
//        Activity activity = new Activity();
//        activity.setActivityCategory(activityCategory);
//        
//        aRepo.save(activity);
//    }
	
	

    
}
