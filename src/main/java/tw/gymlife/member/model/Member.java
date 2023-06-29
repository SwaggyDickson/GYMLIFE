package tw.gymlife.member.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.CommentBean;

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date userBirthDay;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date userRegisterDate;
	private int userTotalSpend = 0;
	private BigDecimal userRewardPoint = BigDecimal.ZERO;
	private String userPermission = "0";
	private int userStatus;
	
	
	   public boolean isVerified() {
		   return userStatus == 1;
	    }
	// 跟文章關聯
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ArticleBean> articles = new ArrayList<>(0);

	// 跟留言關聯
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<CommentBean> comments = new ArrayList<>(0);

	//新增課程訂單
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
//	private List<CorderBean> corder;
//	
	
}
	//接收登入資料
//	public Member(String userAccount, String userPassword) {
//        this.userAccount = userAccount;
//        this.userPassword = userPassword;
//    }
	

	