package tw.gymlife.course.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.course.model.CoachBean;
import tw.gymlife.course.model.CourseBean;
import tw.gymlife.course.model.CourseRepostiory;

@Service
public class courseService {
	@Autowired
	private CourseRepostiory cRepo;
	//新增課程
	public void insertCourse(CourseBean cbean) {
		cRepo.save(cbean);
	}
	//查詢課程byID
	public CourseBean selectCourseById(Integer courseId) {
Optional<CourseBean> optional = cRepo.findById(courseId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	//查詢全部課程
	public List<CourseBean> selectAllCourse() {
		return cRepo.findAll();
	}

	

	//刪除課程
	public void deleteCourse(Integer courseId) {
		cRepo.deleteById(courseId);

	}

	//更新課程
	@Transactional
	public void updateCourse(CoachBean chbean,Integer courseId, String courseName,  String courseIntroduce,
			Integer courseCost) {
		Optional<CourseBean> optional = cRepo.findById(courseId);
		if(optional.isPresent()) {
			CourseBean cbean = optional.get();
			cbean.setCoach(chbean);
			cbean.setCourseName(courseName);
			cbean.setCourseCost(courseCost);
			cbean.setCourseIntroduce(courseIntroduce);
		}
		
		System.out.println("no update data");
		
	}
	//增加觀看人數
	@Transactional
	public void insertCourseViewers(Integer courseId) {
		Optional<CourseBean> optional = cRepo.findById(courseId);
		if(optional.isPresent()) {
			CourseBean cbean = optional.get();
			Integer courseViewers = cbean.getCourseViewers();
			if (courseViewers == null) {
				courseViewers = 0;
			}
			cbean.setCourseViewers(courseViewers+1);
		}
	}
	//增加購買人數
	@Transactional
	public void insertCourseBuyers(Integer courseId) {
		Optional<CourseBean> optional = cRepo.findById(courseId);
		
		if(optional.isPresent()) {
			CourseBean cbean = optional.get();
			Integer courseBuyers = cbean.getCourseBuyers();
			if (courseBuyers == null) {
			    courseBuyers = 0;
			}
			cbean.setCourseBuyers(courseBuyers + 1);
		}
	}


}
