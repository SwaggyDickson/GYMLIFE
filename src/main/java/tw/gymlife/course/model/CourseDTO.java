package tw.gymlife.course.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
@Data
public class CourseDTO {
	private Integer courseId;
	private Integer coachId;
	private String courseName;
	private String courseIntroduce;
	private Integer courseCost;
	private String CoachName;
	private List<Integer> imageId = new ArrayList<>();
	private String coachName;
	private	String coachNickName;
	private String coachPhoneNumber;
	private String coachEmail;
	private String coachBirthday;
	private Integer coachWeight;
	private Integer coachHeight;
	private String coachIntroduce;
//	private Map<Integer,byte[]> cimgs = new HashMap<>();
//	private Map<Integer,CourseBean> cbeans = new HashMap<>();
}
