package tw.gymlife.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
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
@Table(name = "commentlike")
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentLikeId;

	private int liked;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private Member member;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "commentId")
	private CommentBean comment;


}
