package tw.gymlife.course.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoachRepository extends JpaRepository<CoachBean, Integer> {

//	@Query("from CoachBean where courseId = :courseId")
//	public CoachBean selectCoachByCourseId(@Param("courseId")Integer courseId);
}
