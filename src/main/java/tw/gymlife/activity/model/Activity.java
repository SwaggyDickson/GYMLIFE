package tw.gymlife.activity.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

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
	
	@DateTimeFormat(pattern = "yyyy/MM/dd")//時間格式
	@Temporal(TemporalType.DATE)//時間選擇
	@Column(name="activityDate")
	private Date activityDate;
	
	@Column(name="activityLocation")
	private String activityLocation;
	
	@Column(name="activityStatus")
	private String activityStatus;
	
	@Lob
	@Column(name="activityCover")
	private byte[] activityCover;
	
	@Column(name="activityContent")
	private String activityContent;
}
