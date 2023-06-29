package tw.gymlife.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.activity.model.Activity;
import tw.gymlife.activity.model.ActivityRepository;

@Service
public class ActivityService {

	@Autowired
	private ActivityRepository aRepo;
	
	//新增
	public Activity insertActivity(Activity activity) {
		return aRepo.save(activity);
	}
	
	//查單筆(id)
	public Activity findById(Integer activityId) {
		Optional<Activity> optional = aRepo.findById(activityId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//刪除(id)
	public void deleteByid(Integer activityId) {
		aRepo.deleteById(activityId);
	}
	
	//查全部
	public List<Activity> listActivity(){
		return aRepo.findAll();
	}

	//更新
	@Transactional
	public Activity updateActivityById(Integer activityId, String activityName, Date activityDate,
	        String activityLocation, String activityStatus, byte[] activityCover, String activityContent) {
	    Optional<Activity> optional = aRepo.findById(activityId);
	    if (optional.isPresent()) {
	        Activity activity = optional.get();
	        activity.setActivityName(activityName);
	        activity.setActivityDate(activityDate);
	        activity.setActivityLocation(activityLocation);
	        activity.setActivityStatus(activityStatus);
	        activity.setActivityContent(activityContent);

	        if (activityCover != null) {
	            activity.setActivityCover(activityCover);
	        }

	        return aRepo.save(activity);
	    } else {
	        System.out.println("No update data");
	        return null;
	    }
	}
	
	//分頁
	public Page<Activity> findByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 10, Sort.Direction.ASC, "activityId");
		
		Page<Activity> page = aRepo.findAll(pgb);
		
		return page;
	}
		
	}
	
	

