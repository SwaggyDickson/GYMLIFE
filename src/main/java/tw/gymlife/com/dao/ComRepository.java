package tw.gymlife.com.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import tw.gymlife.com.model.Commoditys;

public interface ComRepository extends JpaRepository<Commoditys, Integer> {


	// 關鍵字查詢
	@Query("from Commoditys where comName like %:keyword% and comContent like %:keyword% order by comStatus desc")
	public List<Commoditys> searchByKeyword(@Param(value = "keyword") String keyword);

	//價格排序
	public List<Commoditys> findByOrderByComPriceDesc();
	
	//類別查詢
	public List<Commoditys> findByComTypeContainingOrderByComStatusDesc(String keyword);
	
	//上下架查詢
	public List<Commoditys> findByComStatusContaining(String keyword);
	
	//查詢前五筆資料
	@Query("SELECT c FROM Commoditys c ORDER BY c.comBuyNumber DESC")
	public  List<Commoditys> findTop5ByComBuyNumber();
}
