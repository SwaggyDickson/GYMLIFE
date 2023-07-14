package tw.gymlife.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleReportRepository extends JpaRepository<ArticleReport, Integer> {
	
	ArticleReport findByMemberUserIdAndArticleArticleId(Integer userId, Integer articleId);
	
	List<ArticleReport> findByArticleArticleId(Integer articleId);
	
	 List<ArticleReport> findAllByMemberUserId(int userId);

}
