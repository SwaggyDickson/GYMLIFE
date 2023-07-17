package tw.gymlife.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.gymlife.member.model.Member;

public interface ArticleSaveRepository extends JpaRepository<ArticleSave, Integer> {

	ArticleSave findByMemberUserIdAndArticleArticleId(Integer userId, Integer articleId);

	List<ArticleSave> findByArticleArticleId(Integer articleId);
	
    List<ArticleSave> findAllByMember(Member member);

	
}
