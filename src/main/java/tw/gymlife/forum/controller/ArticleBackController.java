package tw.gymlife.forum.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.annotation.MultipartConfig;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentService;

@Controller
@MultipartConfig
public class ArticleBackController {
	
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	// 查全部 /forum/article/backPage
	@GetMapping("/forum/page")
	public String forumManagePage(Model m) {
		List<ArticleBean> articleBeans = articleService.findAll();
		m.addAttribute("articleBeans", articleBeans);
		return "backgymlife/forum/articleManagePage";
	}
	// ------------------------------更新----------------------------------

	// 文章更新的查詢
	@GetMapping("/forum/edit")
	public String editArticle1(@RequestParam("articleId") Integer articleId, Model model) {
		ArticleBean article = articleService.findById(articleId);
		model.addAttribute("article", article);
		return "backgymlife/forum/articleUpdate";
	}

	// 文章實際更新 (後台更新)
	@PutMapping("/forum/edit")
	public String editArticle2(@RequestParam("articleId") Integer articleId,
			@RequestParam("articleType") String articleType, @RequestParam("articleContent") String articleContent,
			@RequestParam("articleTitle") String articleTitle, Model model,
			@RequestParam("articleImg") MultipartFile articleImg) {
		byte[] byteArr = null;
		try {
			byteArr = articleImg.getBytes(); // 将MultipartFile转换为byte[]
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArticleBean article = articleService.updateArticleById(articleId, articleTitle, articleContent, articleType,
				byteArr);
		model.addAttribute("article", article);
		return "redirect:/forum/page";
	}

	// ------------------------------更新----------------------------------

	// 修改文章狀態
	@PutMapping("/forum/status")
	public String updateStatus(@RequestParam("articleId") Integer articleId, @RequestParam("status") String status,
			Model model) {
		ArticleBean article = articleService.updateArticleStatus(articleId, status);
		model.addAttribute("article", article);
		return "redirect:/forum/page";
	}

	// ------------------------------刪除----------------------------------

	// 真刪除
	@DeleteMapping("/forum/delete")
	public String deleteArticle(@RequestParam("articleId") Integer articleId, Model model) {
		articleService.deleteById(articleId);
		return "redirect:/forum/page"; // 一刪除完回到這一個頁面
	}

	// 假刪除
	@PostMapping("/forum/delete/{articleId}")
	public String deleteArticle(@PathVariable Integer articleId) {
		articleService.disableArticle(articleId);
		System.out.println(666);
		return "redirect:/forum/page";
	}

	// ------------------------------刪除----------------------------------
	
	
	

}

