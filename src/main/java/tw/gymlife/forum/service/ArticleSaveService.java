package tw.gymlife.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.forum.model.ArticleSave;
import tw.gymlife.forum.model.ArticleSaveRepository;

@Service
public class ArticleSaveService {

	@Autowired
	private ArticleSaveRepository articleSaveRepository;

	// 新增
	public void insert(ArticleSave articleSave) {
		articleSaveRepository.save(articleSave);
	}
	
	// 文章收藏查詢
	public ArticleSave findByMemberUserIdAndArticleArticleId(Integer userId, Integer articleId) {
		ArticleSave saved = articleSaveRepository.findByMemberUserIdAndArticleArticleId(userId, articleId);
		return saved;
	}

	public List<ArticleSave> findById(Integer articleId) {
		List<ArticleSave> articleSaveList = articleSaveRepository.findByArticleArticleId(articleId);
		return articleSaveList;
	}
	
}
