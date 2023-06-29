package tw.gymlife.course.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity@Table(name="courseImage")
@Component("courseImage")
public class ImageBean {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="imageId")
    private Integer imageId;
	@Column(name="courseId",insertable = false,updatable = false)
	private Integer courseId;
	@Column(name="imageName")
    private String imageName;
	@JsonIgnore
	@Column(name="imageData")
    private byte[] imageData;
	@Column(name="imgmimeType")
    private String imgmimeType;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId")
	private CourseBean course;
	
	
	public ImageBean() {
		super();
	}
	public ImageBean(Integer imageId, Integer courseId, String imageName, byte[] imageData, String imgmimeType,
			CourseBean course) {
		super();
		this.imageId = imageId;
		this.courseId = courseId;
		this.imageName = imageName;
		this.imageData = imageData;
		this.imgmimeType = imgmimeType;
		this.course = course;
	}
	public Integer getImageId() {
		return imageId;
	}
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public String getImgmimeType() {
		return imgmimeType;
	}
	public void setImgmimeType(String imgmimeType) {
		this.imgmimeType = imgmimeType;
	}
	public CourseBean getCourse() {
		return course;
	}
	public void setCourse(CourseBean course) {
		this.course = course;
	}
	

}
