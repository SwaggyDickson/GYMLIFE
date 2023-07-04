package tw.gymlife.course.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="courseCoach")
public class CoachBean {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="coachId")
	private Integer coachId;
	@Column(name="coachName")
	private String coachName;
	@Column(name="coachNickName")
	private String coachNickName;
	@Column(name="coachPhoneNumber")
	private String coachPhoneNumber;
	@Column(name="coachEmail")
	private String coachEmail;
	@Column(name="coachBirthday")
	private String coachBirthday;
	@Column(name="coachWeight")
	private Integer coachWeight;
	@Column(name="coachHeight")
	private Integer coachHeight;
	@Column(name="coachIntroduce")
	private String coachIntroduce;
	@JsonIgnore
	@Column(name="coachImage")
	private byte[] coachImage;
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coach", cascade = CascadeType.ALL)
    private List<CourseBean> course;
	public CoachBean() {
	}
	public Integer getCoachId() {
		return coachId;
	}
	public void setCoachId(Integer coachId) {
		this.coachId = coachId;
	}
	public String getCoachName() {
		return coachName;
	}
	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}
	public String getCoachPhoneNumber() {
		return coachPhoneNumber;
	}
	public void setCoachPhoneNumber(String coachPhoneNumber) {
		this.coachPhoneNumber = coachPhoneNumber;
	}
	public String getCoachEmail() {
		return coachEmail;
	}
	public void setCoachEmail(String coachEmail) {
		this.coachEmail = coachEmail;
	}
	@JsonFormat(pattern = "yyyy-MM-dd")
	public String getCoachBirthday() {
		return coachBirthday;
	}

	public void setCoachBirthday(String coachBirthday) {
		this.coachBirthday = coachBirthday;
	}
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
	public Integer getCoachWeight() {
		return coachWeight;
	}
	public void setCoachWeight(Integer coachWeight) {
		this.coachWeight = coachWeight;
	}
	public Integer getCoachHeight() {
		return coachHeight;
	}
	public void setCoachHeight(Integer coachHeight) {
		this.coachHeight = coachHeight;
	}
	public String getCoachIntroduce() {
		return coachIntroduce;
	}
	public void setCoachIntroduce(String coachIntroduce) {
		this.coachIntroduce = coachIntroduce;
	}
	public List<CourseBean> getCourse() {
		return course;
	}
	public void setCourse(List<CourseBean> course) {
		this.course = course;
	}
	public String getCoachNickName() {
		return coachNickName;
	}
	public void setCoachNickName(String coachNickName) {
		this.coachNickName = coachNickName;
	}
	public byte[] getCoachImage() {
		return coachImage;
	}
	public void setCoachImage(byte[] coachImage) {
		this.coachImage = coachImage;
	}
	
	
}
