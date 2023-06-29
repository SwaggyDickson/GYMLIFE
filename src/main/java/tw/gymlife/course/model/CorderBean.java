package tw.gymlife.course.model;


import org.springframework.stereotype.Component;

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
	@Column(name="corderQuantity")
	private Integer corderQuantity;
	@Column(name="corderCost")
	private Integer corderCost;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseId")
	private CourseBean course;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Member member;
	public CorderBean() {
		super();
	}


}
