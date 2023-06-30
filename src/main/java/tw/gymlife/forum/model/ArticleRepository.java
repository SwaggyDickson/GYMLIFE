package tw.gymlife.forum.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleBean, Integer> {

	List<ArticleBean> findByStatus(String status);
//	
    List<ArticleBean> findByArticleTypeAndStatus(String articleType, String status);
    
    Page<ArticleBean>  findByStatus(String status, Pageable pageable);

    Page<ArticleBean> findByArticleTypeAndStatus(String articleType, String status, Pageable pageable);

}
