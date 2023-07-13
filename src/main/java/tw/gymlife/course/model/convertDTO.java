package tw.gymlife.course.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tw.gymlife.member.model.Member;

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
			cdto.setCourseViewers(cbean.getCourseViewers());
			cdto.setCourseBuyers(cbean.getCourseBuyers());
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
	//轉ListCorderBean
	public List<CourseDTO> convertCorderDTOList(List<CorderBean> obeans) {
		List<CourseDTO> cdtos = new ArrayList<>();
		for(CorderBean obean:obeans) {
//			System.out.println(cbean.getCoachId());
			CourseDTO cdto = new CourseDTO();
			cdto.setCorderId(obean.getCorderId());
			cdto.setUserId(obean.getUserId());
			cdto.setCourseId(obean.getCourseId());
			cdto.setCorderPayment(obean.getCorderPayment());
			cdto.setCorderTime(obean.getCorderTime());
			cdto.setCorderUpdateTime(obean.getCorderUpdateTime());
			cdto.setCorderQuantity(obean.getCorderQuantity());
			cdto.setCorderCost(obean.getCorderCost());
			cdto.setCourseCost(obean.getCourse().getCourseCost());
			cdto.setCorderState(obean.getCorderState());
			cdto.setCourseName(obean.getCourse().getCourseName());
			cdtos.add(cdto);
		}
		
		return cdtos;
	}
	//轉Member
		public CourseDTO convertMemberDTO(Member member) {
			CourseDTO cdto = new CourseDTO();
			cdto.setUserId(member.getUserId());
			cdto.setUserName(member.getUserName());
			cdto.setUserTel(member.getUserTel());
			cdto.setUserEmail(member.getUserEmail());
			cdto.setCorder(member.getCorder());
			List<String> courseName = new ArrayList<>();
			List<Integer> courseCost = new ArrayList<>();
			for(CorderBean obean :member.getCorder()) {
				courseName.add(obean.getCourse().getCourseName());
				courseCost.add(obean.getCourse().getCourseCost());
			}
			cdto.setCourseNameOrder(courseName);
			cdto.setCourseCostOrder(courseCost);
			
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
