package tw.gymlife.forum.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentService;
import tw.gymlife.member.model.Member;

@Controller
@MultipartConfig
public class CommentBackController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	// 查全部-留言管理頁面
	@GetMapping("/comment/page")
	public String commentManagePage(Model m, HttpSession session) {
	    Member member = (Member) session.getAttribute("member");
	    List<CommentBean> commentBeans;
	    if (member != null) {
	        if (member.getUserPermission().equals("1")) {
	            commentBeans = commentService.findAll();
	            m.addAttribute("commentBeans", commentBeans);
	            m.addAttribute("member", member);  // Add member to the model
	            return "backgymlife/forum/commentManagePage";
	        } else {
	            commentBeans = commentService.findAllByMemberUserId(member.getUserId());
	            m.addAttribute("commentBeans", commentBeans);
	            m.addAttribute("member", member);  // Add member to the model
	            return "backgymlife/forum/commentManagePage";
	        }
	    } else {
	        return "redirect:/Login";
	    }
	}


	// ------------------------------更新----------------------------------

//	// 留言更新的查詢
	@GetMapping("/comment/edit")
	public String editComment1(@RequestParam("commentId") Integer commentId, Model model) {
		CommentBean comment = commentService.findById(commentId);
		model.addAttribute("comment", comment);
		return "backgymlife/forum/commentUpdate";
	}

//	// 留言實際更新 (後台更新)
	@PutMapping("/comment/edit")
	public String editComment2(@RequestParam("commentId") Integer commentId,
			@RequestParam("commentContent") String commentContent, Model model,
			@RequestParam("commentImg") MultipartFile commentImg) {
		byte[] byteArr = null;
		try {
			byteArr = commentImg.getBytes(); // 将MultipartFile转换为byte[]
		} catch (IOException e) {
			e.printStackTrace();
		}
		CommentBean comment = commentService.updateCommentById(commentId, commentContent, byteArr);
		model.addAttribute("comment", comment);
		return "redirect:/comment/page";
	}

//	// ------------------------------更新----------------------------------
//
//	// 修改留言狀態
	@PutMapping("/comment/status")
	public String updateCommentStatus(@RequestParam("commentId") Integer commentId,
			@RequestParam("status") String status, Model model) {
		CommentBean comment = commentService.updateCommentStatus(commentId, status);
		model.addAttribute("comment", comment);
		return "redirect:/comment/page";
	}

	// ------------------------------刪除----------------------------------

	// 真刪除
	@DeleteMapping("/comment/delete")
	public String deleteComment(@RequestParam("commentId") Integer commentId, Model model) {
		commentService.deleteById(commentId);
		return "redirect:/comment/page"; // 一刪除完回到這一個頁面
	}

	// 假刪除
	@PostMapping("/comment/delete/{commentId}")
	public String deleteComment(@PathVariable Integer commentId) {
		commentService.disableComment(commentId);
		return "redirect:/comment/page";
	}

	// ------------------------------刪除----------------------------------

}
