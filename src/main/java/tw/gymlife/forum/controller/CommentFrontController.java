package tw.gymlife.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.CommentLike;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentLikeService;
import tw.gymlife.forum.service.CommentService;
import tw.gymlife.member.model.Member;

@Controller
public class CommentFrontController {

	@Autowired
	private CommentLikeService commentLikeService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	// 留言按讚 //按讚-新版
	@PostMapping("/comment/{commentId}/likes")
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Integer commentId, HttpSession session) {
		try {
			Member member = (Member) session.getAttribute("member");
			CommentBean comment = commentService.findById(commentId);
			Map<String, Object> response = new HashMap<>();

			if (member == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				CommentLike isLiked = commentLikeService.findByMemberUserIdAndCommentCommentId(member.getUserId(), commentId);
				if (isLiked != null) {
					if (isLiked.getLiked() !=0) {
						int likeCount = comment.getLikeCount();
						comment.setLikeCount(likeCount - 1);
						commentService.insert(comment);
					    isLiked.setLiked(0);
						commentLikeService.insert(isLiked);
						response.put("likeCount", comment.getLikeCount());
						response.put("isLiked", isLiked.getLiked());
						return new ResponseEntity<>(response, HttpStatus.OK);
					}else {
						int likeCount = comment.getLikeCount();
						comment.setLikeCount(likeCount + 1);
//						System.out.println("adsadasd"+comment.getLikeCount());
						commentService.insert(comment);
						isLiked.setComment(comment);
						isLiked.setMember(member);
						isLiked.setLiked(1);
						commentLikeService.insert(isLiked);
						likeCount = comment.getLikeCount();
						response.put("likeCount", comment.getLikeCount());
						response.put("isLiked", isLiked.getLiked());
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				} else {
					int likeCount = comment.getLikeCount();
					comment.setLikeCount(likeCount + 1);
					commentService.insert(comment);
					CommentLike like = new CommentLike();
					like.setComment(comment);
					like.setMember(member);
					like.setLiked(1);
					commentLikeService.insert(like);
					response.put("likeCount", comment.getLikeCount());
					response.put("isLiked", like.getLiked());
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
		} catch (Exception e) {
			// 输出错误信息
			e.printStackTrace();
			// 返回500错误
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// ------------------------------以下是巢狀回覆----------------------------------

	// Create a reply
	@PostMapping("/parent/add/{parentCommentId}")
	public String addReply(@RequestParam("articleId") Integer articleId, @PathVariable Integer parentCommentId,
			@RequestParam("commentContent") String commentContent, Model model) {

		CommentBean newReply = new CommentBean();
		newReply.setCommentContent(commentContent);
		// Set other properties as needed...

		CommentBean reply = commentService.addReply(parentCommentId, newReply);
		model.addAttribute("reply", reply);
		return "redirect:/front/" + articleId; // returns reply view
	}

	// Update a reply
//	@PutMapping("/parent/update/{replyId}")
//	public String updateReply(@RequestParam("articleId") Integer articleId,
//	                          @PathVariable Integer replyId,
//	                          @RequestParam("commentContent") String commentContent,
//	                          Model model) {
//	    CommentBean updatedReply = new CommentBean();
//	    updatedReply.setCommentContent(commentContent);
//	    // Set other properties as needed...
//	    CommentBean reply = commentService.updateReply(replyId, updatedReply);
//	    model.addAttribute("reply", reply);
//	    return "redirect:/front/" + articleId; // returns reply view
//	}

	// Update a reply --ajax
	@PutMapping("/parent/update/{replyId}")
	@ResponseBody
	public CommentBean updateReply(@RequestParam("articleId") Integer articleId, @PathVariable Integer replyId,
			@RequestParam("commentContent") String commentContent) {
		CommentBean updatedReply = new CommentBean();
		updatedReply.setCommentContent(commentContent);
		// Set other properties as needed...

		return commentService.updateReply(replyId, updatedReply);
	}

	// Delete a reply
	@DeleteMapping("/parent/delete/{replyId}")
	public String deleteReply(@RequestParam("articleId") Integer articleId, @PathVariable Integer replyId) {
		commentService.deleteReply(replyId);
		return "redirect:/front/" + articleId; // redirect to comments view
	}

	// ------------------------------以上是巢狀回覆----------------------------------

}
