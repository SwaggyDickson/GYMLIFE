package tw.gymlife.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportRepository extends JpaRepository<CommentReport, Integer> {

	CommentReport findByMemberUserIdAndCommentCommentId(Integer userId, Integer commentId);

	List<CommentReport> findByCommentCommentId(Integer commentId);
}
