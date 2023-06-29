package tw.gymlife.forum.model;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentBean, Integer> {

	List<CommentBean> findByArticle(ArticleBean article);

	List<CommentBean> findByStatus(String status);

	
	Page<CommentBean> findByArticleArticleIdAndStatus(Integer articleId, String status, Pageable pageable);

	// 假设你也想对文章类型进行分页
	Page<CommentBean> findByArticleArticleTypeAndStatus(String articleType, String status, Pageable pageable);
}
