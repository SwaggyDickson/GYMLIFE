package tw.gymlife.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.ArticleReport;
import tw.gymlife.forum.model.ArticleReportRepository;
import tw.gymlife.member.service.MailService;

@Service
public class ArticleReportService {

	@Autowired
	private ArticleReportRepository articleReportRepository;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private MailService mailService;

	// 新增
	public void insert(ArticleReport articleReport) {
		articleReportRepository.save(articleReport);
	}

	// 檢舉查詢
	public ArticleReport findByMemberUserIdAndArticleArticleId(Integer userId, Integer articleId) {
		ArticleReport reported = articleReportRepository.findByMemberUserIdAndArticleArticleId(userId, articleId);
		return reported;
	}

	public List<ArticleReport> findById(Integer articleId) {
		List<ArticleReport> articleReportList = articleReportRepository.findByArticleArticleId(articleId);
		return articleReportList;
	}

	public void updateReportCount(ArticleBean article) {
		int reportCount = findById(article.getArticleId()).size();
		article.setReportCount(reportCount);
		articleService.insert(article); // Assuming that insert method is also used for update
	}
}
