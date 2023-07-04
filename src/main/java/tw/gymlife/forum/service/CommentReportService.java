package tw.gymlife.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.forum.model.CommentReport;
import tw.gymlife.forum.model.CommentReportRepository;

@Service
public class CommentReportService {

	@Autowired
	private CommentReportRepository commentReportRepository;

	// 新增
	public void insert(CommentReport commentReport) {
		commentReportRepository.save(commentReport);
	}
	// 按讚
	public CommentReport findByMemberUserIdAndCommentCommentId(Integer userId, Integer commentId) {
		CommentReport liked = commentReportRepository.findByMemberUserIdAndCommentCommentId(userId, commentId);
		return liked;
	}

	public List<CommentReport> findById(Integer commentId) {
		List<CommentReport> commentReportList = commentReportRepository.findByCommentCommentId(commentId);
		return commentReportList;
	}

}
