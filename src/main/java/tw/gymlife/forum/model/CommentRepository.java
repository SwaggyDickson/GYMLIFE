package tw.gymlife.forum.model;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

public interface CommentRepository extends JpaRepository<CommentBean, Integer> {

	List<CommentBean> findByArticle(ArticleBean article);

	List<CommentBean> findByStatus(String status);

//    @Transactional
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
//	@Query("SELECT c FROM CommentBean c WHERE c.commentId = :commentId")
//	CommentBean findByCommentIdForUpdate(@Param("commentId") Integer commentId);

	Page<CommentBean> findByArticleArticleIdAndStatus(Integer articleId, String status, Pageable pageable);

	// 假设你也想对文章类型进行分页
	Page<CommentBean> findByArticleArticleTypeAndStatus(String articleType, String status, Pageable pageable);
	
    List<CommentBean> findByParentCommentId(Integer parentCommentId);
    
    
    List<CommentBean> findAllByMemberUserId(int userId);
}
