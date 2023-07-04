package tw.gymlife.com.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tw.gymlife.com.model.Orders;

public interface OrderRepository extends JpaRepository<Orders, Integer> {

	// 找符合userId與時間的資料
	Optional<Orders> findByUserIdAndOrderTime(int userId, String orderTime);

	// 找出該會員的所有訂單資訊
	List<Orders> findByUserId(int userId);

	// 找出除了以付款的所有訂單資訊
	@Query(" FROM Orders  WHERE orderPayment <> 1 ORDER BY orderStatusTime")
	List<Orders> findOrdersWithPaymentNotEqualToOneOrderByStatusTime();
}
