package tw.gymlife.forum.controller;

import tw.gymlife.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.processor.SpringInputCheckboxFieldTagProcessor;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.CommentLike;
import tw.gymlife.forum.service.ArticleLikeService;
import tw.gymlife.forum.service.ArticleService;
import tw.gymlife.forum.service.CommentLikeService;
import tw.gymlife.forum.service.CommentService;

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

	// 測試頁面
//	@GetMapping("/front")
//	public String testFront() {
//		return "FrontGYMLIFE/forum/gymlife";
//	}

	// 文章按讚
	@ResponseBody
	@PostMapping("/article/{articleId}/likes")
	public void toggleLike2(@PathVariable Integer articleId, @RequestParam Integer userId) {
		articleLikeService.toggleLike(userId, articleId);
	}

//	@PostMapping("/front/{articleId}/{commentId}/likes")
//	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Integer commentId, HttpSession session) {
//	    Member member = (Member) session.getAttribute("member");
//
//	    if (member == null) {
//	        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//	    }
//
//	    commentLikeService.toggleLike(member.getUserId(), commentId);
//
//	    int likeCount = commentLikeService.getLikeCount(commentId);
//	    boolean liked = commentLikeService.isLiked(member.getUserId(), commentId);
//
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("likeCount", likeCount);
//	    response.put("liked", liked);
//
//	    return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	// ------------------------------首頁----------------------------------

//	// 透過左上角主題分類，跳轉至各主題的首頁
	@GetMapping("/front/active/{articleType}")
	public String listActiveArticlesByType(@PathVariable String articleType,
			@RequestParam(name = "p", defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3") int pageSize, Model model) {
		Page<ArticleBean> articleBeans = articleService.findActiveArticlesByType(articleType, pageNumber, pageSize);
		model.addAttribute("articleBeans", articleBeans);
//		model.addAttribute("totalPages", articleBeans.getTotalPages()); // 新添加的代码
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
		Integer userId = (Integer) session.getAttribute("userId");
		ArticleBean article = articleService.findById(articleId);
		boolean isLoggedIn = userId != null;
		Page<CommentBean> comments = commentService.findActiveCommentsByArticleId(articleId, pageNumber, pageSize);
	
		Map<Integer, List<CommentBean>> commentReplies = new HashMap<>();
		// 按讚-新版
		Map<Integer, Boolean> userLikedComments = new HashMap<>();
	    Map<Integer, Integer> likeCounts = new HashMap<>();  // New Map to store like counts

		for (CommentBean comment : comments.getContent()) {
			// Check if the comment is liked by the current user
			CommentLike liked = commentLikeService.findByMemberUserIdAndCommentCommentId(userId,
					comment.getCommentId());
			// If the CommentLike object exists and the liked field is 1, then the user has
			// liked this comment
			boolean userLikedThisComment = liked != null && liked.getLiked() == 1;
			userLikedComments.put(comment.getCommentId(), userLikedThisComment);

			 // Get like count for each comment
	        int likeCount = commentLikeService.getLikeCount(comment.getCommentId());
	        likeCounts.put(comment.getCommentId(), likeCount);
			
			
			//回復
			List<CommentBean> replies = commentService.findRepliesByCommentId(comment.getCommentId());
			commentReplies.put(comment.getCommentId(), replies);
		}
		model.addAttribute("article", article);
		model.addAttribute("comments", comments);
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalComments", comments.getTotalElements());
		model.addAttribute("commentReplies", commentReplies);

		// Add userLikedComments to the model
	    model.addAttribute("userLikedComments", userLikedComments);
	    model.addAttribute("likeCounts", likeCounts);  // Add likeCounts to the model

	    
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
	public String insertCommentPage(Model model, @PathVariable Integer articleId
	// , @RequestParam("commentId")Integer commentId
	) {
		ArticleBean article = articleService.findById(articleId);
		// List<CommentBean> comment = article.getComments();
		model.addAttribute("article", article);
		// model.addAttribute("comment", comment); // 这是一个新的、空的评论对象
		return "frontgymlife/forum/commentInsert";
	}

	// 新增留言-跳頁
	@PostMapping("/front/article/{articleId}/comment")
	public String addComment(@PathVariable Integer articleId, @RequestParam String commentContent,
			@RequestParam("commentImg") MultipartFile commentImg, Model model) {
		ArticleBean article = articleService.findById(articleId);
		CommentBean comment = new CommentBean();
		comment.setCommentContent(commentContent);
		try {
			// 轉換並設定圖片
			byte[] byteArr = commentImg.getBytes();
			comment.setCommentImg(byteArr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		comment.setArticle(article);
		commentService.insert(comment);
		System.out.println(articleId);
		model.addAttribute("article", article);
		model.addAttribute("comment", comment);
		return "redirect:/front/" + articleId; // 回到文章內頁
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

	// 新增留言-aJax
//	@PostMapping("/front/article/{articleId}/comment")
//	@ResponseBody
//	public Map<String, Object> addComment3(@PathVariable Integer articleId, 
//			@RequestParam String commentContent, Model model) {
//	    ArticleBean article = articleService.findById(articleId);
//	    CommentBean comment = new CommentBean();
//	    comment.setCommentContent(commentContent);
//	    comment.setArticle(article);
//	    commentService.insert(comment);
//
//	    Map<String, Object> result = new HashMap();
//	    result.put("commentId", comment.getCommentId());
//	    result.put("commentContent", comment.getCommentContent());
//	    result.put("commentTime", comment.getCommentTimeString());
//
//	    return result;  // return the newly created comment
//	}

	// ------------------------------更新----------------------------------

	// 實際更新留言 (old)
//	@ResponseBody
//	@PutMapping("/front/comments/{commentId}")
//	public ResponseEntity<CommentBean> updateComment(@PathVariable Integer commentId, @RequestBody CommentBean updatedComment) {
//	    System.out.println("Received commentId: " + commentId); // 查看接收到的评论 ID
//	    System.out.println("Received updatedComment: " + updatedComment); // 查看接收到的更新评论
//
//	    CommentBean Comment = commentService.findById(commentId);
//
//	    if (Comment == null) {
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
//
//	    Comment.setCommentContent(updatedComment.getCommentContent());
//	    Comment.setCommentImg(Base64.getDecoder().decode(updatedComment.getCommentImg()));
//	    Comment.setCommentUpdateTime(new Date());
//
//	    commentService.updateCommentById(commentId, Comment.getCommentContent(), Comment.getCommentImg(), Comment.getCommentUpdateTime());
//
//	    return new ResponseEntity<>(Comment, HttpStatus.OK);
//	}

	// 實際更新留言 (now)
//	@ResponseBody
//	@PutMapping("/front/comments/{commentId}")
//	public CommentBean updateComment( @RequestParam("commentId")Integer commentId,
//			@PathVariable Integer commentId,
////			@RequestBody CommentBean updatedComment
//			@RequestParam ("commentContent")String commentContent,@RequestParam(name="commentImg",required = false) MultipartFile commentImg ) {
////	    System.out.println("Received commentId: " + commentId); // 查看接收到的评论 ID
////	    System.out.println("Received updatedComment: " + updatedComment); // 查看接收到的更新评论
//
//	    CommentBean Comment = commentService.findById(commentId);
//
//	    if (Comment == null) {
//	        return null;
//	    }
//
//	  // Comment.setCommentContent(commentContent);
//	   byte[] byteArr = null;
//		try {
//			byteArr = commentImg.getBytes(); // 将MultipartFile转换为byte[]
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	    Comment.setCommentUpdateTime(new Date());
//
//	     commentService.updateCommentById(commentId, commentContent, commentImg);
//
//	    return Comment;
//	}

	// 實際更新留言-aJax
//	@ResponseBody
//	@PutMapping("/front/comments/{commentId}")
//	public CommentBean updateComment(
//	    @PathVariable Integer commentId,
//	    @RequestParam("commentContent") String commentContent,
//	    @RequestParam(name="commentImg",required = false) MultipartFile commentImg
//	) {
//	    byte[] byteArr = null;
//	    if (commentImg != null) {
//	        try {
//	            byteArr = commentImg.getBytes(); // 将MultipartFile转换为byte[]
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	    }
//	    CommentBean updatedComment = commentService.updateCommentById(commentId, commentContent, byteArr);
//	    return updatedComment;
//	}

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
