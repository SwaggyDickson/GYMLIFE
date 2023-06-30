package tw.gymlife.forum.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.forum.model.ArticleBean;
import tw.gymlife.forum.model.ArticleLike;
import tw.gymlife.forum.model.ArticleLikeRepository;
import tw.gymlife.forum.model.ArticleRepository;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class ArticleLikeService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleLikeRepository articleLikeRepository;

	public void toggleLike(Integer userId, Integer articleId) {
	    Optional<Member> memberOptional = memberRepository.findById(userId);
	    Optional<ArticleBean> articleOptional = articleRepository.findById(articleId);

	    if(memberOptional.isPresent() && articleOptional.isPresent()) {
	        Member member = memberOptional.get();
	        ArticleBean article = articleOptional.get();

	        ArticleLike like = articleLikeRepository.findByMemberAndArticle(member, article);
	        if (like == null) {
	            like = new ArticleLike();
	            like.setMember(member);
	            like.setArticle(article);
	        }
	        like.setLiked(!like.isLiked());
	        articleLikeRepository.save(like);
	    } else {
	        // 在這裡處理找不到 Member 或 ArticleBean 的情況
	    }
	}

}
