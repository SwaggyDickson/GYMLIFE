package tw.gymlife.member.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import tw.gymlife.com.model.Orders;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.ArticleLike;
import tw.gymlife.forum.model.CommentLike;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.ArticleReport;
import tw.gymlife.forum.model.CommentReport;
import tw.gymlife.forum.model.ArticleSave;
import tw.gymlife.activity.model.Registration;

@Data
@Entity
@Table(name = "members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;

	@Size(min = 8, max = 20, message = "帳號必須在8到20個英數字之內")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "帳號只能使用英文數字及字母")
	private String userAccount;
	private String userPassword;
	private String userName;
	private String userNickName;
	private String userGender;
	private String userAddress;
	private String userTel;
	private String userEmail;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date userBirthDay;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date userRegisterDate;
	private int userTotalSpend = 0;
	private BigDecimal userRewardPoint = BigDecimal.ZERO;
	private String userPermission = "0";
	private int userStatus;
	private byte[] userPhoto;

	public boolean isVerified() {
		return userStatus == 1;
	}
	public String getFormattedUserBirthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(userBirthDay);
    }
	public String getFormattedUserRegisterDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(userRegisterDate);
    }

	// 跟文章關聯
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ArticleBean> articles = new ArrayList<>(0);

	// 跟留言關聯
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<CommentBean> comments = new ArrayList<>(0);

	// 文章按讚
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ArticleLike> articleLikes = new ArrayList<>();

	// 留言按讚
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<CommentLike> commentLikes = new ArrayList<>();

	// 文章檢舉
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ArticleReport> articleReports = new ArrayList<>();

	// 留言檢舉
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<CommentReport> commentReports = new ArrayList<>();

	// 文章收藏
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ArticleSave> articleSaves = new ArrayList<>();
	
	// 會員一對多商品訂單
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "members", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Orders> orders = new ArrayList<>(); 
	
	// 新增課程訂單
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CorderBean> corder;
	
	// 會員一對多報名
	@JsonIgnore
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Registration> registrations = new ArrayList<>();
	
	

}
// 接收登入資料
//	public Member(String userAccount, String userPassword) {
//        this.userAccount = userAccount;
//        this.userPassword = userPassword;
//    }
