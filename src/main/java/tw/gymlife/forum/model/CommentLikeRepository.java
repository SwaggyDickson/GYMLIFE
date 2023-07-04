package tw.gymlife.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import tw.gymlife.member.model.Member;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
//    CommentLike findByMemberAndComment(Member member, CommentBean comment);
//
//    int countByCommentCommentId(Integer commentId);
//    
//    List<CommentLike> findByMemberUserId(Integer userId);
    
//    @Transactional
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT c FROM CommentLike c WHERE c.member.userId = :userId")
//    CommentLike findByMemberUserIdForUpdate(@Param("userId") Integer userId);
//    
//    int countByCommentCommentIdAndLiked(Integer commentId, boolean liked);
    
    //根據會員id和留言id來做查詢
    CommentLike findByMemberUserIdAndCommentCommentId(Integer userId, Integer commentId);
    
    List<CommentLike> findByCommentCommentId(Integer commentId);
    
    
}
