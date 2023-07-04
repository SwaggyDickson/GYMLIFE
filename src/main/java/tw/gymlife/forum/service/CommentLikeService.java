package tw.gymlife.forum.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import tw.gymlife.forum.model.CommentBean;
import tw.gymlife.forum.model.CommentLike;
import tw.gymlife.forum.model.CommentRepository;
import tw.gymlife.forum.model.CommentLikeRepository;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class CommentLikeService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CommentLikeRepository commentLikeRepository;

	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(CommentLikeService.class);

	@Autowired
	private EntityManager entityManager;

	//新增
	public void insert(CommentLike commentLike) {
		commentLikeRepository.save(commentLike);
	}
	
	
	// 按讚
	public CommentLike findByMemberUserIdAndCommentCommentId(Integer userId, Integer commentId) {
		CommentLike liked = commentLikeRepository.findByMemberUserIdAndCommentCommentId(userId, commentId);
		return liked;
	}

	public List<CommentLike> findById(Integer commentId) {
		List<CommentLike> commentLikeList = commentLikeRepository.findByCommentCommentId(commentId);
		return commentLikeList;
	}
	
	public int getLikeCount(Integer commentId) {
		List<CommentLike> commentLikes = commentLikeRepository.findByCommentCommentId(commentId);
		int likeCount = 0;
		for (CommentLike like : commentLikes) {
			if (like.getLiked() == 1) {
				likeCount++;
			}
		}
		return likeCount;
	}


}
