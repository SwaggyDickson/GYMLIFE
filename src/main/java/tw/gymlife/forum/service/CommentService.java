package tw.gymlife.forum.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.CommentRepository;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	// 新增
	public void insert(CommentBean CommentBean) {
		// CommentBean.setCommentTime(new Date()); // 设置评论时间
		commentRepository.save(CommentBean);
	}

	// ------------------------------查詢----------------------------------

	// Pageable 第二個參數是每頁幾筆，要知道目前在第幾頁(那一頁就不能點選)
	public Page<CommentBean> findByPage(Integer pageNumber) {
		// 如頁數(起始頁面)、每頁顯示的數量和排序方式(3-4)等。
		Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "articleTime");
		Page<CommentBean> page = commentRepository.findAll(pgb);
		return page;
	}

	//文章內頁
	public Page<CommentBean> findActiveCommentsByArticleId(Integer articleId, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "commentTime");
		Page<CommentBean> page =commentRepository.findByArticleArticleIdAndStatus(articleId, "Active", pageable);
		return page;
	}

	public List<CommentBean> findCommentsByArticle(ArticleBean article) {
		return commentRepository.findByArticle(article);
	}

	// 單筆查詢
	public CommentBean findById(Integer commentId) {
		Optional<CommentBean> optional = commentRepository.findById(commentId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 查看所有文章
	public List<CommentBean> findAll() {
		return commentRepository.findAll();
	}

	// 在前台論壇首頁只查看active狀態的留言
	public List<CommentBean> findActiveComments() {
		return commentRepository.findByStatus("Active");
	}

	// ----------------------------------------------------------------

	// 更新評論
	@Transactional
	public CommentBean updateCommentById(Integer commentId, String commentContent, byte[] commentImg) {
		Optional<CommentBean> optional = commentRepository.findById(commentId);
		if (optional.isPresent()) {
			CommentBean CommentBean = optional.get();
			CommentBean.setCommentContent(commentContent);
			CommentBean.setCommentImg(commentImg);
			CommentBean.setCommentUpdateTime(new Date()); // 设置评论更新时间
			commentRepository.save(CommentBean);
			return CommentBean;
		}
		System.out.println("no update data");
		return null;
	}

	// 更新留言狀態
	@Transactional
	public CommentBean updateCommentStatus(Integer commentId, String status) {
		Optional<CommentBean> optional = commentRepository.findById(commentId);

		if (optional.isPresent()) {
			CommentBean CommentBean = optional.get();
			CommentBean.setStatus(status);
			commentRepository.save(CommentBean);
			return CommentBean;
		} else {
			// Handle the case where the article does not exist...
			return null;
		}
	}

	// ------------------------------刪除----------------------------------

	// 真刪除評論
	public void deleteById(Integer commentId) {
		commentRepository.deleteById(commentId);
	}

	// 假刪除評論
	@Transactional
	public void disableComment(Integer commentId) {
		Optional<CommentBean> optional = commentRepository.findById(commentId);
		System.out.println("Deleting commentId:" + commentId);
		if (optional.isPresent()) {
			CommentBean CommentBean = optional.get();
			CommentBean.setStatus("Disabled");
			commentRepository.save(CommentBean);
		}
	}
	// ------------------------------刪除----------------------------------

//		public CommentBean findLastest() {
//			return commentRepository.findFirstByOrderByAddedDesc();
//		}
	//

}
