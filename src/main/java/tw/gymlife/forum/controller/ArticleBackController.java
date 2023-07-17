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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.ArticleReport;
import tw.gymlife.forum.service.ArticleReportService;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentService;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MailService;

@Controller
@MultipartConfig
public class ArticleBackController {
	
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ArticleReportService articleReportService;
	
	@Autowired
	private MailService mailService;
	
	
	@PostMapping("/admin/article/report/send/{reportId}")
	public String sendReportMail(@PathVariable Integer reportId, RedirectAttributes redirectAttributes) {
	    try {
	        // 獲取檢舉
	        ArticleReport report = articleReportService.findById2(reportId);
	        if (report == null) {
	            redirectAttributes.addFlashAttribute("message", "檢舉不存在");
	            return "redirect:/report/page";
	        }

	        // 獲取會員資訊
	        Member member = report.getMember();
	        ArticleBean article = report.getArticle();

	        // 發送檢舉郵件
	        String recipient = member.getUserEmail(); // 取得會員的電子郵件地址
	        String subject = "您的文章已被檢舉"; // 設定郵件主題
	        String message = "您的文章 " + article.getArticleTitle() + " 已被檢舉。請檢視並改善您的文章。"; // 設定郵件內容
	        mailService.prepareAndSend(recipient, subject, message); // 這行呼叫mailService的prepareAndSend方法來發送電子郵件

	        // 若一切正常，返回一個包含成功訊息的響應
	        redirectAttributes.addFlashAttribute("message", "郵件發送成功");
	    } catch (Exception e) {
	        // 輸出錯誤訊息
	        e.printStackTrace();
	        // 添加錯誤訊息
	        redirectAttributes.addFlashAttribute("message", "發送郵件失敗");
	    }
	    return "redirect:/report/page";
	}

	
	
	//更新檢舉狀態
	@PostMapping("/updateReportStatus")
    public String updateReportStatus(Integer reportId, String newStatus, RedirectAttributes redirectAttributes) {
		articleReportService.updateReportStatus(reportId, newStatus);
        redirectAttributes.addFlashAttribute("message", "檢舉狀態已更新");
        return "redirect:/report/page";
    }
	
	
	//檢舉後台
	@GetMapping("/report/page")
	public String reportManagePage(Model m, HttpSession session) {
	    Member member = (Member) session.getAttribute("member");
	    List<ArticleReport> articleReports;
//	    if (member != null) {
//	        if (member.getUserPermission().equals("1")) {
	        	articleReports = articleReportService.findAll();
////	        } else {
//	        	articleReports = articleReportService.findAllByMemberUserId(member.getUserId());
//	        }
	        m.addAttribute("articleReports", articleReports);
//	        m.addAttribute("member", member);  // Add member to the model
	        return "backgymlife/forum/reportManagePage";
//	    } else {
//	        return "redirect:/Login";
//	    }
	}
	
	
	
	
	

	//文章後台
	@GetMapping("/forum/page")
	public String forumManagePage(Model m, HttpSession session) {
	    Member member = (Member) session.getAttribute("member");
	    List<ArticleBean> articleBeans;
	    if (member != null) {
	        if (member.getUserPermission().equals("1")) {
	            articleBeans = articleService.findAll();
	        } else {
	            articleBeans = articleService.findAllByMemberUserId(member.getUserId());
	        }
	        m.addAttribute("articleBeans", articleBeans);
	        m.addAttribute("member", member);  // Add member to the model
	        return "backgymlife/forum/articleManagePage";
	    } else {
	        return "redirect:/Login";
	    }
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
		return "redirect:/forum/page";
	}

	// ------------------------------刪除----------------------------------

}

