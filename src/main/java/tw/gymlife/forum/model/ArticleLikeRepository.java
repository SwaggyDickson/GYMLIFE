package tw.gymlife.forum.model;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.gymlife.member.model.Member;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {

	//ArticleLike findByUserIdAndArticleId(Integer userId, Integer articleId);

	ArticleLike findByMemberAndArticle(Member member, ArticleBean article);

}
