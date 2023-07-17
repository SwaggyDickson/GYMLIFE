package tw.gymlife.forum.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "articleReport")
public class ArticleReport {

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
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private ArticleBean article;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private Member member;
    
    
}
