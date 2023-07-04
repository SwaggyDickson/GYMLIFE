package tw.gymlife.course.model;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
@Component
public class CourseBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "courseId")
	private Integer courseId;
	@Column(name="coachId",insertable = false,updatable = false)
	private Integer coachId;
	@Column(name = "courseName")
	private String courseName;
	@Column(name = "courseIntroduce")
	private String courseIntroduce;
	@Column(name = "courseCost")
	private Integer courseCost;
	@Column(name = "courseViewers")
	private Integer courseViewers = 0;
	@Column(name = "courseBuyers")
	private Integer courseBuyers = 0;
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
	private List<ImageBean> images;
	@JsonManagedReference
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coachId")
    private CoachBean coach;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
	private List<CorderBean> corder;
	public CourseBean() {
	}

	@Override
	public String toString() {
		return "CourseBean [courseId=" + courseId + ", coachId=" + coachId + ", courseName=" + courseName
				+ ", courseIntroduce=" + courseIntroduce + ", courseCost=" + courseCost + ", images=" + images
				+ ", coach=" + coach + "]";
	}

	public CourseBean(Integer courseId, String courseName, String coachName, String courseIntroduce,
			Integer courseCost) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseIntroduce = courseIntroduce;
		this.courseCost = courseCost;
	}

	public Integer getCoachId() {
		return coachId;
	}

	public void setCoachId(Integer coachId) {
		this.coachId = coachId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public String getCourseIntroduce() {
		return courseIntroduce;
	}

	public void setCourseIntroduce(String courseIntroduce) {
		this.courseIntroduce = courseIntroduce;
	}

	public Integer getCourseCost() {
		return courseCost;
	}

	public void setCourseCost(Integer courseCost) {
		this.courseCost = courseCost;
	}

	public List<ImageBean> getImages() {
		return images;
	}

	public void setImages(List<ImageBean> images) {
		this.images = images;
	}

	public CoachBean getCoach() {
		return coach;
	}

	public void setCoach(CoachBean coach) {
		this.coach = coach;
	}

	public Integer getCourseViewers() {
		return courseViewers;
	}

	public void setCourseViewers(Integer courseViewers) {
		this.courseViewers = courseViewers;
	}

	public Integer getCourseBuyers() {
		return courseBuyers;
	}

	public void setCourseBuyers(Integer courseBuyers) {
		this.courseBuyers = courseBuyers;
	}

	public List<CorderBean> getCorder() {
		return corder;
	}

	public void setCorder(List<CorderBean> corder) {
		this.corder = corder;
	}

}