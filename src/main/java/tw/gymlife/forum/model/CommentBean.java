package tw.gymlife.forum.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "comment")
public class CommentBean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentId;

	private String commentContent;

	@Lob
	private byte[] commentImg;

	private int likeCount = 0;
	
	private int reportCount;

	private Integer parentCommentId; 

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentTime;

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentUpdateTime;

	// 留言狀態
	@Column(name = "status", nullable = false)
	private String status = "Active"; // 默认状态为 Active (預設值)

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "articleId")
	private ArticleBean article;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private Member member;
	
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private List<CommentLike> commentLikes = new ArrayList<>();
	
	@OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
	private List<CommentReport> commentReports = new ArrayList<>();

	@PrePersist // 當物件轉換成persist狀態以前，要做的事情放在方法裡面
	public void onCreate() {
		if (commentTime == null) {
			commentTime = new Date();
		}
	}

	public String getCommentTimeString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(this.commentTime);
	}

	public boolean isActive() {
		return "Active".equals(this.status);
	}
	
	@Override
	public String toString() {
	    return "CommentBean{" +
	            "commentId=" + commentId +
	            ", commentContent='" + commentContent + '\'' +
	            ", commentTime=" + commentTime +
	            ", status=" + status +
	            // Note: Not including 'article' here
	            '}';
	}

}
