package tw.gymlife.activity.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.gymlife.activity.model.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

	//根據活動ID查詢活動詳細資料
	@Query("from Registration where activityId = :activityId")
	public List<Registration> findByActivityId(@Param("activityId")Integer activityId);


	//根據會員ID查詢活動詳細資料
	@Query("from Registration where userId = :userId")
	public List<Registration> findByuserId(@Param("userId")Integer userId);
}



