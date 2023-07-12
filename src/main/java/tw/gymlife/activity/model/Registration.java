package tw.gymlife.activity.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import tw.gymlife.member.model.Member;

@Data
@Entity
@Table(name="registration")
public class Registration {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="registrationId")
	private Integer registrationId;
	
	@Column(name="activityId",insertable = false,updatable = false)
	private Integer activityId;
	
	@Column(name="userId",insertable = false,updatable = false)
	private Integer userId;
	
    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "activityId")
    private Activity activity;
	
	@Column(name="registrationStatus")
	private String registrationStatus;
	
	@Column(name="registrationDate")
	private Date registrationDate;
	
	@Column(name="emergencyContact")
	private String emergencyContact;
	
	@Column(name="emergencyContactPhone")
	private Integer emergencyContactPhone;
	
	@Column(name="relationship")
	private String relationship;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")//時間格式
	@Temporal(TemporalType.TIMESTAMP)//時間選擇
	@Column(name="createTime", updatable = false)
	private Date createTime;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")//時間格式
	@Temporal(TemporalType.TIMESTAMP)//時間選擇
	@Column(name="updateTime")
	private Date updateTime;
	
    @PrePersist
    public void prePersist() {
        this.createTime = new Date();
    }
	
}
