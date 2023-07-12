package tw.gymlife.activity.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.springframework.format.annotation.DateTimeFormat;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import tw.gymlife.member.model.Member;

@Data
@Entity
@Table(name="activity")
public class Activity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="activityId")
	private Integer activityId;
	
	@Column(name="activityName")
	private String activityName; 
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")//時間格式
	@Temporal(TemporalType.DATE)//時間選擇
	@Column(name="activityDate")
	private Date activityDate;
	
	@Column(name="activityLocation")
	private String activityLocation;
	
	@Column(name="activityCategory")
	private String activityCategory;
	
	@Lob
	@Column(name="activityCover")
	private byte[] activityCover;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")//時間格式
	@Temporal(TemporalType.DATE)//時間選擇
	@Column(name="registrationStartDate")
	private Date registrationStartDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")//時間格式
	@Temporal(TemporalType.DATE)//時間選擇
	@Column(name="registrationEndDate")
	private Date registrationEndDate;
	
	@Column(name="organizer")
	private String organizer;
	
	@Column(name="activityStatus")
	private String activityStatus;
	
	@Column(name = "activityInfo")
	private String activityInfo;
	
	@Column(name = "applicantLimitedQty")
	private Integer applicantLimitedQty;
	
	@Column(name = "activityApplicantsQty")
	private Integer activityApplicantsQty;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")//時間格式
	@Temporal(TemporalType.TIMESTAMP)//時間選擇
	@Column(name="createTime", updatable = false)
	private Date createTime;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")//時間格式
	@Temporal(TemporalType.TIMESTAMP)//時間選擇
	@Column(name="updateTime")
	private Date updateTime;
    
    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL,orphanRemoval = true )
    private List<Registration> registrations = new ArrayList<>();;
    
    
    @PrePersist
    public void prePersist() {
        this.createTime = new Date();
    }
    
    
    
//    @PreUpdate
//    public void preUpdate() {
//        this.updateTime = new Date();
//    }
//	
}
