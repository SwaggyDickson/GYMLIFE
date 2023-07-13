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

	// 找出除了 已付款(1)與 已出貨(4)的所有訂單資訊  //0=處理中, 1=已付款, 2=商家確認訂單中, 3=訂單已更新, 4=已發貨
	@Query("FROM Orders WHERE orderPayment NOT IN (1, 4, 3) ORDER BY orderStatusTime")
	List<Orders> findOrdersWithPaymentNotEqualToOneOrderByStatusTime();
	
	//用UUID找訂單
	Optional<Orders> findByOrderUuid(String uuid);
}
