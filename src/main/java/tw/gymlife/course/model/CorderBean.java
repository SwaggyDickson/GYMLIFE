package tw.gymlife.course.model;


import java.util.Date;

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
import lombok.Data;
import tw.gymlife.member.model.Member;

@Data
@Entity
@Table(name="corder")
public class CorderBean {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="corderId")
	private Integer corderId;
	@Column(name="userId",insertable = false,updatable = false)
	private Integer userId;
	@Column(name="courseId",insertable = false,updatable = false)
	private Integer courseId;
	@Column(name="corderPayment")
	private String corderPayment;
	@Column(name="corderTime",insertable = false,updatable = false)
	private String corderTime;
	@Column(name="corderUpdateTime")
	private String corderUpdateTime;
	@Column(name="corderQuantity")
	private Integer corderQuantity;
	@Column(name="corderCost")
	private Integer corderCost;
	@Column(name="corderState")
	private Integer corderState;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId")
	private CourseBean course;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Member member;
	public CorderBean() {
		super();
	}


}
