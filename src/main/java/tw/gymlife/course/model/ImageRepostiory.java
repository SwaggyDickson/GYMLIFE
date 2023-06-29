package tw.gymlife.course.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepostiory extends JpaRepository<ImageBean, Integer> {
	
	@Query("from ImageBean where courseId = :courseId")
	public List<ImageBean> selectCourseImageById(@Param("courseId")Integer courseId);
	
}
