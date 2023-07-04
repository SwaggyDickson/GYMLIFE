package tw.gymlife.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.forum.model.ArticleLike;
import tw.gymlife.forum.model.ArticleLikeRepository;
import tw.gymlife.forum.model.ArticleRepository;
import tw.gymlife.member.model.MemberRepository;

@Service
public class ArticleLikeService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleLikeRepository articleLikeRepository;

	// 新增
	public void insert(ArticleLike articleLike) {
		articleLikeRepository.save(articleLike);
	}
	
	// 按讚查詢
	public ArticleLike findByMemberUserIdAndArticleArticleId(Integer userId, Integer articleId) {
		ArticleLike liked = articleLikeRepository.findByMemberUserIdAndArticleArticleId(userId, articleId);
		return liked;
	}

	public List<ArticleLike> findById(Integer articleId) {
		List<ArticleLike> articleLikeList = articleLikeRepository.findByArticleArticleId(articleId);
		return articleLikeList;
	}

	public int getLikeCount(Integer articleId) {
		List<ArticleLike> articleLikes = articleLikeRepository.findByArticleArticleId(articleId);
		int likeCount = 0;
		for (ArticleLike like : articleLikes) {
			if (like.getLiked() == 1) {
				likeCount++;
			}
		}
		return likeCount;
	}

}
