package tw.gymlife.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name="articlelike")
public class ArticleLike {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer articleLikeId;

  private int liked;
  
  @ManyToOne
  @JoinColumn(name = "userId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "articleId")
  private ArticleBean article;
  
  
}

