package tw.gymlife.forum.model;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tw.gymlife.member.model.Member;

@Data
@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true) // 使我們可以使用 builder 模式來創建新的 CommentLike 對象
@Table(name = "commentlike")
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentLikeId;

//  @Builder.Default // 確保在使用 builder 模式創建新的 CommentLike 對象時，isLiked 屬性被初始化為 false
	private int liked;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private Member member;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "commentId")
	private CommentBean comment;

//  public Integer getCommentId() {
//      return comment.getCommentId();
//  }
//  

}
