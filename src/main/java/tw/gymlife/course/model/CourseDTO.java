package tw.gymlife.course.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
	private Integer corderId;
	private Integer userId;
	private String corderPayment;
	private String corderTime;
	private String corderUpdateTime;
	private Integer corderQuantity;
	private Integer corderCost;
	private Integer corderState;
	private Integer courseViewers;
	private Integer courseBuyers;
    private String userName;
    private String userTel;
    private String userEmail;
    private List<CorderBean> corder;
    private List<String> courseNameOrder;
    private List<Integer> courseCostOrder;
	public int getAge(String birthday) {
        if (birthday != null) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        	LocalDate birthdate = LocalDate.parse(birthday, formatter);
            LocalDate now = LocalDate.now();
            Period age = Period.between(birthdate, now);
            return age.getYears();
        }
        return 0;
    }
//	private Map<Integer,byte[]> cimgs = new HashMap<>();
//	private Map<Integer,CourseBean> cbeans = new HashMap<>();
}
