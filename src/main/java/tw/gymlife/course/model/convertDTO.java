package tw.gymlife.course.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class convertDTO {
	//轉ListCourseBean
	public List<CourseDTO> convertCourseDTOList(List<CourseBean> cbeans) {
		List<CourseDTO> cdtos = new ArrayList<>();
		for(CourseBean cbean:cbeans) {
//			System.out.println(cbean.getCoachId());
			CourseDTO cdto = new CourseDTO();
			cdto.setCourseId(cbean.getCourseId());
			cdto.setCoachId(cbean.getCoach().getCoachId());
			cdto.setCourseName(cbean.getCourseName());
			cdto.setCourseCost(cbean.getCourseCost());
			cdto.setCourseIntroduce(cbean.getCourseIntroduce());
			cdto.setCoachName(cbean.getCoach().getCoachName());
			List<Integer> imageIds = new ArrayList<>();
			for(ImageBean img: cbean.getImages()) {
				imageIds.add(img.getImageId());
			}
			cdto.setImageId(imageIds);
			cdtos.add(cdto);
		}
		
		return cdtos;
	}
	//轉CourseBean
	public CourseDTO convertCourseDTO(CourseBean cbean) {
		CourseDTO cdto = new CourseDTO();
			cdto.setCourseId(cbean.getCourseId());
			cdto.setCourseName(cbean.getCourseName());
			cdto.setCourseCost(cbean.getCourseCost());
			cdto.setCourseIntroduce(cbean.getCourseIntroduce());
			cdto.setCoachId(cbean.getCoach().getCoachId());
			cdto.setCoachName(cbean.getCoach().getCoachName());
			cdto.setCoachNickName(cbean.getCoach().getCoachNickName());
			cdto.setCoachPhoneNumber(cbean.getCoach().getCoachPhoneNumber());
			cdto.setCoachEmail(cbean.getCoach().getCoachEmail());
			cdto.setCoachBirthday(cbean.getCoach().getCoachBirthday());
			cdto.setCoachWeight(cbean.getCoach().getCoachWeight());
			cdto.setCoachHeight(cbean.getCoach().getCoachHeight());
			cdto.setCoachIntroduce(cbean.getCoach().getCoachIntroduce());
			List<Integer> imageIds = new ArrayList<>();
			for(ImageBean img: cbean.getImages()) {
				imageIds.add(img.getImageId());
			}
			cdto.setImageId(imageIds);
		
		return cdto;
	}
//	public List<CoachDTO> convertCoachDTO(List<CoachBean> chbeans) {
//		List<CoachDTO> chdtos = new ArrayList<>();
//		for(CoachBean chbean:chbeans) {
//			CoachDTO cdto = new CoachDTO();
//			cdto.setCoachId(chbean.getCoachId());
//			cdto.setCoachName(chbean.getCoachName());
//			cdto.setCoachNickName(chbean.getCoachNickName());
//			cdto.setCoachPhoneNumber(chbean.getCoachPhoneNumber());
//			cdto.setCoachEmail(chbean.getCoachEmail());
//			cdto.setCoachBirthday(chbean.getCoachBirthday());
//			cdto.setCoachWeight(chbean.getCoachWeight());
//			cdto.setCoachHeight(chbean.getCoachHeight());
//			cdto.setCoachIntroduce(chbean.getCoachIntroduce());
//			List<Integer> courseId = new ArrayList<>();
//			List<String> courseName = new ArrayList<>();
//			List<Integer> courseCost = new ArrayList<>();
//			List<String> courseIntroduce = new ArrayList<>();
//			for(CourseBean cbean: chbean.getCourse()) {
//				courseId.add(cbean.getCourseId());
//				courseName.add(cbean.getCourseName());
//				courseCost.add(cbean.getCourseCost());
//				courseIntroduce.add(cbean.getCourseIntroduce());
//			}
//			cdto.setCourseId(courseId);
//			cdto.setCourseName(courseName);
//			cdto.setCourseCost(courseCost);
//			cdto.setCourseIntroduce(courseIntroduce);
//			chdtos.add(cdto);
//		}
//		
//		return chdtos;
//	}
}
