package tw.gymlife.com.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.gymlife.com.model.Commoditys;

public interface ComFrontRepository extends JpaRepository<Commoditys, Integer> {

	
	//查詢所有上架商品
	 List<Commoditys> findByComStatus(String comStatus);
	 
	 //價格排序
	 List<Commoditys> findByComStatusOrderByComPriceDesc(String comStatus);
	 
	 //關鍵字查詢(JS)
	 @Query(value = "SELECT * FROM Commoditys  WHERE comStatus = :comStatus AND comName LIKE %:comName% ESCAPE '\\'",nativeQuery=true)
	 List<Commoditys> findByComStatusAndComNameLike(@Param("comStatus") String comStatus, @Param("comName") String comName);
	 
	 //類別查詢
	 @Query(value = "SELECT * FROM Commoditys  WHERE comStatus = :comStatus AND comType LIKE %:comType% ESCAPE '\\'",nativeQuery=true)
	 List<Commoditys> findByComStatusAndComTypeLike(@Param("comStatus") String comStatus, @Param("comType") String comType);
	 
	 //前三筆最多人觀看商品
	 
	 @Query(value = "Select Top 3 * From Commoditys WHERE comStatus = :comStatus Order By clicktime ", nativeQuery = true)
	 List<Commoditys> getTopThreeCommoditys(@Param("comStatus")String comStatus);
	 
}