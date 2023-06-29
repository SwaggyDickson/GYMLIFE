package tw.gymlife.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.course.model.CoachBean;
import tw.gymlife.course.model.CoachRepository;

@Service
public class coachService {

	@Autowired
	private CoachRepository chRepo;
	//新增教練
	public void insertCourseCoach(CoachBean chbean) {
		chRepo.save(chbean);
	}
	//查詢全部教練
	public List<CoachBean> selectAllCoach(){
		return chRepo.findAll();
	}
	//查詢教練byID
	public CoachBean selectCoachById(Integer coachId) {
		Optional<CoachBean> optional = chRepo.findById(coachId);
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
//	//查詢教練by CourseId
//	public CoachBean selectCoachByCourseId(Integer courseId) {
//		CoachBean chbean = chRepo.selectCoachByCourseId(courseId);
//		return chbean;
//	}
	//修改教練資訊
	@Transactional
	public void updateCourseCoach(Integer coachId,String coachName,String coachNickName,String coachPhoneNumber,String coachEmail, String coachBirthday,Integer coachWeight,Integer coachHeight,String coachIntroduce) {
		Optional<CoachBean> optional = chRepo.findById(coachId);
		if(optional.isPresent()) {
			CoachBean chbean = optional.get();
			chbean.setCoachName(coachName);
			chbean.setCoachNickName(coachNickName);
			chbean.setCoachPhoneNumber(coachPhoneNumber);
			chbean.setCoachEmail(coachEmail);
			chbean.setCoachBirthday(coachBirthday);
			chbean.setCoachWeight(coachWeight);
			chbean.setCoachHeight(coachHeight);
			chbean.setCoachIntroduce(coachIntroduce);
		}
	}
	//修改教練照片
	@Transactional
	public void updateCoachImg(Integer coachId,byte[] coachImg) {
		Optional<CoachBean> optional = chRepo.findById(coachId);
		if(optional.isPresent()) {
			CoachBean chbean = optional.get();
			chbean.setCoachImage(coachImg);
			
		}
	}
	//刪除教練
	public void deleteCoach(Integer coachId) {
		chRepo.deleteById(coachId);
	}
}
