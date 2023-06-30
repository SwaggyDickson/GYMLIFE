package tw.gymlife.com.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.gymlife.com.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer>{

	
	//找符合userId與時間的資料
	Optional<Orders> findByUserIdAndOrderTime(int userId, String orderTime);
	
	//找出該會員的所有訂單資訊
	 List<Orders> findByUserId(int userId);
}
