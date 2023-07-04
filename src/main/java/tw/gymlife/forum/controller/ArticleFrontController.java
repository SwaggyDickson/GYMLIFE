package tw.gymlife.forum.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.ArticleLike;
import tw.gymlife.forum.model.ArticleReport;
import tw.gymlife.forum.model.ArticleSave;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.CommentLike;
import tw.gymlife.forum.service.ArticleLikeService;
import tw.gymlife.forum.service.ArticleReportService;
import tw.gymlife.forum.service.ArticleSaveService;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentLikeService;
import tw.gymlife.forum.service.CommentReportService;
import tw.gymlife.forum.service.CommentService;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.service.MailService;

@Controller
@MultipartConfig
public class ArticleFrontController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ArticleLikeService articleLikeService;

	@Autowired
	private CommentLikeService commentLikeService;

	@Autowired
	private ArticleReportService articleReportService;

	@Autowired
	private CommentReportService commentReportService;

	@Autowired
	private ArticleSaveService articleSaveService;

	@Autowired
	private MailService mailService;

	// 測試頁面
//	@GetMapping("/front")
//	public String testFront() {
//		return "FrontGYMLIFE/forum/gymlife";
//	}

	// 文章收藏
	@PostMapping("/article/{articleId}/save")
	public ResponseEntity<Map<String, Object>> toggleArticleSave(@PathVariable Integer articleId, HttpSession session) {
	    try {
	        Member member = (Member) session.getAttribute("member");
	        ArticleBean article = articleService.findById(articleId);
	        Map<String, Object> response = new HashMap<>();
	        if (member == null) {
	            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        } else {
	            ArticleSave isSaved = articleSaveService.findByMemberUserIdAndArticleArticleId(member.getUserId(),
	                    articleId);
	            // 已收藏
	            if (isSaved != null) {
	                if (isSaved.getSaved() != 0) {
	                    // 已收藏，取消收藏
	                    isSaved.setSaved(0);
	                    articleSaveService.insert(isSaved);
	                    response.put("isSaved", isSaved.getSaved() == 1);  // 修改這裡
	                    return new ResponseEntity<>(response, HttpStatus.OK);
	                } else {
	                    // 之前取消過收藏，重新收藏
	                    isSaved.setSaved(1);
	                    articleSaveService.insert(isSaved);
	                    response.put("isSaved", isSaved.getSaved() == 1);  // 修改這裡
	                    return new ResponseEntity<>(response, HttpStatus.OK);
	                }
	            } else {
	                // 從未收藏，添加收藏
	                ArticleSave save = new ArticleSave();
	                save.setArticle(article);
	                save.setMember(member);
	                save.setSaved(1);
	                articleSaveService.insert(save);
	                response.put("isSaved", save.getSaved() == 1);  // 修正這裡，使用 save 而不是 isSaved
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


	// 文章檢舉-ajax
	@PostMapping("/article/{articleId}/report")
	public ResponseEntity<Map<String, Object>> reportArticle(@PathVariable Integer articleId,
			@RequestBody ArticleReport report, HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		try {
			Member member = (Member) session.getAttribute("member");

			// 檢查是否已登入
			if (member == null) {
				response.put("message", "請先登入再進行檢舉");
				return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 或者可以改成重定向到登入頁面
			}

			// 檢查該會員是否已經檢舉過這篇文章
			ArticleReport existingReport = articleReportService
					.findByMemberUserIdAndArticleArticleId(member.getUserId(), articleId);
			if (existingReport != null) {
				response.put("message", "你已經檢舉過這篇文章，每個會員只能檢舉一次");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			ArticleBean article = articleService.findById(articleId);

			// 檢查是否檢舉自己的文章
			if (member.getUserId() == (article.getMember().getUserId())) {
				response.put("message", "不能檢舉自己的文章");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			// 新的檢舉
			report.setArticle(article);
			report.setMember(member);
			report.setReportTime(new Date());
			report.setReportStatus("Reported"); // 改變你實際的被檢舉狀態
			articleReportService.insert(report);

			// 更新檢舉次數
			articleReportService.updateReportCount(article);

			// 如果檢舉次數達到或超過5次，則發送電子郵件
			if (article.getReportCount() >= 1) {
				String recipient = member.getUserEmail(); // 取得會員的電子郵件地址
				String subject = "您的文章已被檢舉5次"; // 設定郵件主題
				String message = "您的文章 " + article.getArticleTitle() + " 已被檢舉超過5次。請檢視並改善您的文章。"; // 設定郵件內容

				mailService.prepareAndSend(recipient, subject, message); // 發送電子郵件
			}

			// 若一切正常，返回一個包含成功訊息的響應
			response.put("message", "檢舉成功");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			// 輸出錯誤訊息
			e.printStackTrace();
			// 返回500錯誤
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 文章按讚
	@PostMapping("/article/{articleId}/likes")
	public ResponseEntity<Map<String, Object>> toggleArticleLike(@PathVariable Integer articleId, HttpSession session) {
		try {
			Member member = (Member) session.getAttribute("member");
			ArticleBean article = articleService.findById(articleId);
			Map<String, Object> response = new HashMap<>();
			if (member == null) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} else {
				ArticleLike isLiked = articleLikeService.findByMemberUserIdAndArticleArticleId(member.getUserId(),
						articleId);
				// 按過讚
				if (isLiked != null) {
					// 這邊是看按讚狀態去做+-讚數並更改狀態
					if (isLiked.getLiked() != 0) {
						// 曾經按過讚
						int likeCount = article.getLikeCount();
						article.setLikeCount(likeCount - 1);
						articleService.insert(article);
						isLiked.setLiked(0);
						articleLikeService.insert(isLiked);
						response.put("likeCount", article.getLikeCount());
						response.put("isLiked", isLiked.getLiked());
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						// 可能曾經按過卻收回讚
						int likeCount = article.getLikeCount();
						article.setLikeCount(likeCount + 1);
						articleService.insert(article);
						isLiked.setArticle(article);
						isLiked.setMember(member);
						isLiked.setLiked(1);
						articleLikeService.insert(isLiked);
						likeCount = article.getLikeCount();
						response.put("likeCount", article.getLikeCount());
						response.put("isLiked", isLiked.getLiked());
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				} else {
					// 從沒按過讚
					int likeCount = article.getLikeCount();
					article.setLikeCount(likeCount + 1);
					articleService.insert(article);
					ArticleLike like = new ArticleLike();
					like.setArticle(article);
					like.setMember(member);
					like.setLiked(1);
					articleLikeService.insert(like);
					response.put("likeCount", article.getLikeCount());
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

	// ------------------------------首頁----------------------------------

//	// 透過左上角主題分類，跳轉至各主題的首頁
	@GetMapping("/front/active/{articleType}")
	public String listActiveArticlesByType(@PathVariable String articleType,
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3") int pageSize, Model model) {
		Page<ArticleBean> articleBeans = articleService.findActiveArticlesByType(articleType, pageNumber, pageSize);
		model.addAttribute("articleBeans", articleBeans);
		return "frontgymlife/forum/articleFrontPage";
	}

//	// 查詢active狀態的文章 (之後都redirect回首頁)
	@GetMapping("/front/active")
	public String listActiveArticles(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3") int pageSize, Model model) {
		Page<ArticleBean> articleBeans = articleService.findActiveArticles(pageNumber, pageSize);
		model.addAttribute("articleBeans", articleBeans);
//		model.addAttribute("totalPages", articleBeans.getTotalPages()); // 新添加的代码
		return "frontgymlife/forum/articleFrontPage";
	}

	// 查看文章內頁 -只顯示active狀態的文章、留言， ----------------- 回覆還沒設定，是真刪除
	@GetMapping("/front/{articleId}")
	public String showArticleDetail(@PathVariable Integer articleId,
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3") int pageSize, Model model, HttpSession session) {
		Integer loggedInUserId = (Integer) session.getAttribute("userId");
		ArticleBean article = articleService.findById(articleId);
		Integer articleUserId = article.getMember().getUserId(); // 獲取文章作者的ID

		boolean isLoggedIn = loggedInUserId != null; // 還沒設定model，但是用戶未登入時候能看到他人按過的總讚數

		// 獲取文章的當前瀏覽次數
		Integer viewCount = article.getViewCount();

		// 將瀏覽次數加一
		viewCount++;

		// 更新瀏覽次數到文章物件
		article.setViewCount(viewCount);

		// 將更新後的瀏覽次數保存回資料庫
		articleService.insert(article);

		Page<CommentBean> comments = commentService.findActiveCommentsByArticleId(articleId, pageNumber, pageSize);

		Map<Integer, List<CommentBean>> commentReplies = new HashMap<>();
		// 留言按讚-新版
		Map<Integer, Boolean> userLikedComments = new HashMap<>();
		Map<Integer, Integer> likeCounts = new HashMap<>(); // New Map to store like counts

		for (CommentBean comment : comments.getContent()) {
			// Check if the comment is liked by the current user
			CommentLike liked = commentLikeService.findByMemberUserIdAndCommentCommentId(loggedInUserId,
					comment.getCommentId());
			// If the CommentLike object exists and the liked field is 1, then the user has
			// liked this comment
			boolean userLikedThisComment = liked != null && liked.getLiked() == 1;
			userLikedComments.put(comment.getCommentId(), userLikedThisComment);

			// Get like count for each comment
			int likeCount = commentLikeService.getLikeCount(comment.getCommentId());
			likeCounts.put(comment.getCommentId(), likeCount);

			// 回復
			List<CommentBean> replies = commentService.findRepliesByCommentId(comment.getCommentId());
			commentReplies.put(comment.getCommentId(), replies);
		}

		// Article like
		ArticleLike articleLiked = articleLikeService.findByMemberUserIdAndArticleArticleId(loggedInUserId, articleId);
		boolean userLikedThisArticle = articleLiked != null && articleLiked.getLiked() == 1;
		int articleLikeCount = articleLikeService.getLikeCount(articleId);
		
		// 文章收藏
		ArticleSave articleSaved = articleSaveService.findByMemberUserIdAndArticleArticleId(loggedInUserId, articleId);
		boolean userSavedThisArticle = articleSaved != null && articleSaved.getSaved() == 1;


		model.addAttribute("article", article);
		model.addAttribute("comments", comments);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalComments", comments.getTotalElements());
		model.addAttribute("commentReplies", commentReplies);

		// Add userLikedComments to the model 留言按讚
		model.addAttribute("userLikedComments", userLikedComments);
		model.addAttribute("likeCounts", likeCounts); // Add likeCounts to the model

		// Add article like info to the model 文章按讚
		model.addAttribute("userLikedArticle", userLikedThisArticle);
		model.addAttribute("articleLikeCount", articleLikeCount);

		// 不能檢舉自己
		model.addAttribute("loggedInUserId", loggedInUserId);
		model.addAttribute("articleUserId", articleUserId);
		
		// 文章收藏
		model.addAttribute("userSavedArticle", userSavedThisArticle);

		return "frontgymlife/forum/articleInnerPage";
	}

	// ------------------------------首頁----------------------------------

	// 新增文章頁面
	@GetMapping("/front/insertPage")
	public String insertArticlePage(HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		return "frontgymlife/forum/articleInsert";
	}

	// 實際新增貼文
	@PostMapping("/front/insert")
	public String insertArticle(HttpSession session, @RequestParam("articleTitle") String articleTitle,
			@RequestParam("articleContent") String articleContent, @RequestParam("articleType") String articleType,
			@RequestParam("articleImg") MultipartFile file, Model model) throws IOException {
		Member member = (Member) session.getAttribute("member");
		ArticleBean article = new ArticleBean();
		article.setArticleTitle(articleTitle);
		article.setArticleContent(articleContent);
		article.setArticleType(articleType);
		article.setArticleImg(file.getBytes());
		article.setMember(member);
		articleService.insert(article);
		model.addAttribute("article", article);
		model.addAttribute("member", member);
		return "redirect:/front/active";
	}

	// ------------------------------更新文章----------------------------------

	// 文章更新的查詢
	@GetMapping("/front/articleEdit")
	public String editArticle1(@RequestParam("articleId") Integer articleId, Model model) {
		ArticleBean article = articleService.findById(articleId);
		model.addAttribute("article", article);
		return "frontgymlife/forum/articleEdit";
	}

	// 文章實際更新 (前台更新)
	@PutMapping("/front/articleEdit")
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
		return "redirect:/front/" + articleId; // 回到文章內頁
	}

	// ------------------------------更新文章----------------------------------

	// ------------------------------刪除文章----------------------------------

	// 真刪除文章(前台)
	@DeleteMapping("/front/article/delete")
	public String deleteArticle(@RequestParam("articleId") Integer articleId, Model model) {
		articleService.deleteById(articleId);
		return "redirect:/front/active"; // 回到論壇首頁
	}

	// 假刪除文章(前台)
	@PostMapping("/front/article/delete/{articleId}")
	public String deleteArticle(@PathVariable Integer articleId) {
		articleService.disableArticle(articleId);
		return "redirect:/front/active"; // 回到論壇首頁
	}

	// ------------------------------刪除文章----------------------------------

	// 新增留言頁面-查詢
	@GetMapping("/front/{articleId}/insertCommentPage")
	public String insertCommentPage(Model model, @PathVariable Integer articleId, HttpSession session
	// , @RequestParam("commentId")Integer commentId
	) {
		Member member = (Member) session.getAttribute("member");
		ArticleBean article = articleService.findById(articleId);
		// List<CommentBean> comment = article.getComments();
		model.addAttribute("article", article);
		// model.addAttribute("comment", comment); // 这是一个新的、空的评论对象
		return "frontgymlife/forum/commentInsert";
	}

	// 新增留言-跳頁
	@PostMapping("/front/article/{articleId}/comment")
	public String addComment(@PathVariable Integer articleId, @RequestParam String commentContent,
			@RequestParam("commentImg") MultipartFile commentImg, Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		ArticleBean article = articleService.findById(articleId);
		CommentBean comment = new CommentBean();
		comment.setCommentContent(commentContent);
		try {
			// 转换并设置图片
			byte[] byteArr = commentImg.getBytes();
			comment.setCommentImg(byteArr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		comment.setArticle(article);
		comment.setMember(member); // 设置评论所属的会员
		commentService.insert(comment);
		System.out.println(articleId);
		model.addAttribute("article", article);
		model.addAttribute("comment", comment);
		return "redirect:/front/" + articleId; // 返回文章内页
	}

	// ------------------------------刪除留言----------------------------------

	// 刪除留言--真刪除
	@DeleteMapping("/front/{articleId}/delete")
	public String deleteArticle(@PathVariable Integer articleId, @RequestParam("commentId") Integer commentId,
			Model model) {
		commentService.deleteById(commentId);
		return "redirect:/front/" + articleId; // 一刪除完回到文章頁面
	}

	// 刪除留言--假刪除
	@PostMapping("/front/{articleId}/comment/delete")
	public String deleteArticle2(@PathVariable Integer articleId, @RequestParam("commentId") Integer commentId,
			Model model) {
		commentService.disableComment(commentId);
		return "redirect:/front/" + articleId; // 一刪除完回到文章頁面
	}

	// ------------------------------刪除留言----------------------------------

	// ------------------------------更新----------------------------------

	// 更新-留言查詢：跳轉的方式
	@GetMapping("/front/comments/edit")
	public String editComment(@RequestParam("commentId") Integer commentId, Model model) {
		CommentBean comment = commentService.findById(commentId);
		ArticleBean article = comment.getArticle();
		model.addAttribute("article", article);
		model.addAttribute("comment", comment);
		return "frontgymlife/forum/commentEdit"; // 跳轉到更新留言的頁面
	}

	// ------------------------------更新----------------------------------

	// 更新-留言更新：跳轉的方式
	@PutMapping("/front/comments/edit")
	public String editComment2(@RequestParam("commentId") Integer commentId,
			@RequestParam("commentContent") String commentContent, Model model,
			@RequestParam("articleId") Integer articleId, @RequestParam("commentImg") MultipartFile commentImg) {
		byte[] byteArr = null;
		try {
			byteArr = commentImg.getBytes(); // 将MultipartFile转换为byte[]
		} catch (IOException e) {
			e.printStackTrace();
		}
//		ArticleBean article = articleService.findById(commentId);
//		model.addAttribute("article", article);
		CommentBean comment = commentService.updateCommentById(commentId, commentContent, byteArr);
		model.addAttribute("comment", comment);
		return "redirect:/front/" + articleId; // 更新之後，跳轉回，文章內頁
	}

	// ------------------------------處理圖片----------------------------------

	// 輸出圖片，以供下載 (有人要顯示在螢幕上就要用這個方法，透過字串相加，靜態+動態的方式處理二進位圖片)
	@GetMapping("/downloadImage/{articleId}")
	// ResponseEntity 不受@Controller回傳頁面限制影響，可以透握header來設定回傳格式type
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer articleId) {

		// 存在本地端，只有以下這兩行的取得方式會不同
		ArticleBean photo1 = articleService.findById(articleId);
		byte[] photoFile = photo1.getArticleImg();

		// 透過這個controler來 透過HttpHeaders來設置content type
		HttpHeaders headers = new HttpHeaders();
		// 註解掉ContentType會變成avif
		headers.setContentType(MediaType.IMAGE_JPEG);

		// 1. 檔案 2. headers 3. HttpStatus
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}

	// 輸出圖片，以供下載 (有人要顯示在螢幕上就要用這個方法，透過字串相加，靜態+動態的方式處理二進位圖片)
	@GetMapping("/front/comments/{commentId}")
	// th:src="@{'/front/comments/'+${comment.commentId}}"
	// ResponseEntity 不受@Controller回傳頁面限制影響，可以透握header來設定回傳格式type
	public ResponseEntity<byte[]> downloadImage2(@PathVariable Integer commentId) {

		// 存在本地端，只有以下這兩行的取得方式會不同
		CommentBean photo1 = commentService.findById(commentId);
		byte[] photoFile = photo1.getCommentImg();

		// 透過這個controler來 透過HttpHeaders來設置content type
		HttpHeaders headers = new HttpHeaders();
		// 註解掉ContentType會變成avif
		headers.setContentType(MediaType.IMAGE_JPEG);

		// 1. 檔案 2. headers 3. HttpStatus
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}

	// ---------------------------------------------------------------------

}
