package tw.gymlife.activity.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="activityImage")
public class ActivityImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="imageId")
	private Integer imageId;
	
	@Lob
	@Column(name="photoFile")
	private byte[] photoFile;
	
	@JsonBackReference // 不由這邊做對面JSON序列化
    @ManyToOne
    @JoinColumn(name = "activityId")
    private Activity activity;


}
