package tw.gymlife.forum.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
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
@Table(name = "article")
public class ArticleBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "articleId")
	private Integer articleId;

	@Column(name = "articleTitle")
	private String articleTitle;

	@Column(name = "articleType")
	private String articleType; // 文章主題分類

	@Column(name = "articleContent")
	private String articleContent;

	private Boolean hasImage;
	
	@Lob
	@Column(name = "articleImg")
	private byte[] articleImg; // 待修正 byte[]
	
	private int likeCount = 0;
	
	private int reportCount;
	
	private int viewCount; //文章瀏覽次數

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "articleTime")
	private Date articleTime; // 文章新增時間

	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "articleUpdateTime")
	private Date articleUpdateTime; // 文章編輯時間

//	private Integer articleFollow; //追蹤貼文

	// 文章狀態
	@Column(name = "status", nullable = false)
	private String status = "Active"; // 默认状态为 Active (預設值)

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CommentBean> comments;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private Member member;

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ArticleLike> articleLikes = new ArrayList<>();

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ArticleReport> articleReports = new ArrayList<>();
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	private List<ArticleSave> articleSaves = new ArrayList<>();
	
	@PrePersist // 當物件轉換成persist狀態以前，要做的事情放在方法裡面
	public void onCreate() {
		if (articleTime == null) {
			articleTime = new Date();
		}

	}
	@Override
	public String toString() {
		return "ArticleBean [articleId=" + articleId + ", articleTitle=" + articleTitle + ", articleType=" + articleType
				+ ", articleContent=" + articleContent + ", articleImg=" + Arrays.toString(articleImg) + ", likeCount="
				+ likeCount + ", articleTime=" + articleTime + ", articleUpdateTime=" + articleUpdateTime + ", status="
				+ status + ", comments=" + comments + ", member=" + member + ", articleLikes=" + articleLikes + "]";
	}
}
