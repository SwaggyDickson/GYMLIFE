package tw.gymlife.forum.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import tw.gymlife.member.model.Member;

@Data
@Entity
@Table(name = "commentReport")
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @Column(name = "reportReason", columnDefinition = "NVARCHAR(MAX)")
    private String reportReason;

    @Column(name = "reportTime")
    private Date reportTime;
    
    private String reportType;

    @Column(name = "reportStatus", columnDefinition = "NVARCHAR(MAX)")
    private String reportStatus;

    @Column(name = "isNestedComment")
    private boolean isNestedComment;
    
    @ManyToOne
    @JoinColumn(name = "commentId")
    private CommentBean comment;
    
    @ManyToOne
    @JoinColumn(name = "parentCommentId")
    private CommentBean parentComment;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member;
}

