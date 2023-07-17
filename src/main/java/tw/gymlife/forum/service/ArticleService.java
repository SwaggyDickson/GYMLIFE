package tw.gymlife.forum.service;

import java.util.ArrayList;
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
import tw.gymlife.forum.model.ArticleRepository;
import tw.gymlife.forum.model.ArticleSave;
import tw.gymlife.member.model.Member;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleSaveService articleSaveService;

	public List<ArticleBean> findSavedArticlesByMember(Member member) {
		// 這裡假設你已有方法可以獲取所有保存的文章
		List<ArticleSave> savedArticles = articleSaveService.findAllByMember(member);
		List<ArticleBean> articles = new ArrayList<>();

		for (ArticleSave savedArticle : savedArticles) {
			ArticleBean article = savedArticle.getArticle();
			article.getArticleSaves().add(savedArticle);
			articles.add(article);
		}

		return articles;
	}

	public ArticleBean findByMemberUserIdAndArticleId(Integer userId, Integer articleId) {
		ArticleBean article = articleRepository.findByMemberUserIdAndArticleId(userId, articleId);
		return article;
	}

	// 透過會員編號查詢該會員文章按最新到最舊排列，會員個人頁面
	public Page<ArticleBean> findByMemberUserIdOrderByArticleIdDesc(Integer userId, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.Direction.DESC, "articleId");
		Page<ArticleBean> articles = articleRepository.findByMemberUserId(userId, pageable);
		return articles;
	}

	public List<ArticleBean> findAllByMemberUserId(int userId) {
		return articleRepository.findAllByMemberUserId(userId);
	}

	// 新增
	public void insert(ArticleBean articleBean) {
		articleRepository.save(articleBean);
	}

	// ------------------------------查詢----------------------------------

	// 分頁功能
	// Pageable 第二個參數是每頁幾筆，要知道目前在第幾頁(那一頁就不能點選)
	public Page<ArticleBean> findByPage(Integer pageNumber) {
		// 如頁數(起始頁面)、每頁顯示的數量和排序方式(3-4)等。
		Pageable pgb = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "articleTime");
		Page<ArticleBean> page = articleRepository.findAll(pgb);
		return page;
	}

	// 查詢所有符合active 且種類相同的文章
	public List<ArticleBean> findActiveArticlesByType(String articleType) {
		return articleRepository.findByArticleTypeAndStatus(articleType, "active");
	}

	// 在前台論壇首頁只查看active狀態的文章
	public List<ArticleBean> findActiveArticles() {
		return articleRepository.findByStatus("Active");
	}

	// 查詢所有符合active且種類相同的文章
	public Page<ArticleBean> findActiveArticlesByType(String articleType, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "articleTime");
		Page<ArticleBean> page = articleRepository.findByArticleTypeAndStatus(articleType, "active", pageable);
		return page;
	}

	// ------------------------------篩選----------------------------------

	// 在前台論壇首頁只查看active狀態的文章
	// desc
	public Page<ArticleBean> findActiveArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "articleTime");
		Page<ArticleBean> page = articleRepository.findByStatus("Active", pageable);
		return page;
	}

	// asc
	public Page<ArticleBean> findOlderArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.ASC, "articleTime");
		Page<ArticleBean> page = articleRepository.findByStatus("Active", pageable);
		return page;
	}

	// mostViews
	public Page<ArticleBean> findMostViews(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "viewCount");
		Page<ArticleBean> page = articleRepository.findByStatus("Active", pageable);
		return page;
	}

//	public Page<ArticleBean> findMostViews(int pageNumber, int pageSize) {
//	    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//	    Page<ArticleBean> page = articleRepository.findByStatusOrderByViewCountDesc("Active", pageable);
//	    return page;
//	}

	// mostLikes
	public Page<ArticleBean> findMostLikedArticles(int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 3, Sort.Direction.DESC, "likeCount");
		Page<ArticleBean> page = articleRepository.findByStatus("Active", pageable);
		return page;
	}

//	public Page<ArticleBean> findMostLikedArticles(int pageNumber, int pageSize) {
//	    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//	    return articleRepository.findByStatusOrderByLikeCountDesc("Active", pageable);
//	}

	// 為推薦功能添加的方法，需要根據你的推薦算法來實現 隨機推薦相關主題的
//	public Page<ArticleBean> findRecommendedArticles(int pageNumber, int pageSize) {
//	    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
//	    return articleRepository.findByStatusAndRecommended("Active", pageable);
//	}

	// ------------------------------篩選----------------------------------

	// 單筆查詢
	public ArticleBean findById(Integer articleId) {
		Optional<ArticleBean> optional = articleRepository.findById(articleId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 查看所有文章
	public List<ArticleBean> findAll() {
		return articleRepository.findAll();
	}

	// ------------------------------更新----------------------------------

	// 更新文章
	@Transactional
	public ArticleBean updateArticleById(Integer articleId, String articleTitle, String articleContent,
			String articleType, byte[] articleImg) {
		Optional<ArticleBean> optional = articleRepository.findById(articleId);

		if (optional.isPresent()) {
			ArticleBean articleBean = optional.get();
			articleBean.setArticleTitle(articleTitle); // 需要設定傳入的值，而非原本的值
			articleBean.setArticleContent(articleContent); // 需要設定傳入的值，而非原本的值
			articleBean.setArticleType(articleType); // 需要設定傳入的值，而非原本的值
			articleBean.setArticleImg(articleImg); // 需要設定傳入的值，而非原本的值
			articleBean.setArticleUpdateTime(new Date());
			articleRepository.save(articleBean);
			return articleBean;
		}
		System.out.println("no update data");
		return null;
	}

	// 更新文章狀態
	@Transactional
	public ArticleBean updateArticleStatus(Integer articleId, String status) {
		Optional<ArticleBean> optional = articleRepository.findById(articleId);

		if (optional.isPresent()) {
			ArticleBean articleBean = optional.get();
			articleBean.setStatus(status);
			articleRepository.save(articleBean);
			return articleBean;
		} else {
			// Handle the case where the article does not exist...
			return null;
		}
	}

	// ------------------------------刪除----------------------------------

	// 真刪除
	public void deleteById(Integer articleId) {
		articleRepository.deleteById(articleId);
	}

	// 假刪除
	@Transactional
	public void disableArticle(Integer articleId) {
		Optional<ArticleBean> optional = articleRepository.findById(articleId);
		System.out.println("Deleting articleId:" + articleId);
		if (optional.isPresent()) {
			ArticleBean articleBean = optional.get();
			articleBean.setStatus("Disabled");
			articleRepository.save(articleBean);
		}
	}

	// ------------------------------刪除----------------------------------

}
