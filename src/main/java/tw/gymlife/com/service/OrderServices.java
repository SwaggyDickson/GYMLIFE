package tw.gymlife.com.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.OrderRepository;
import tw.gymlife.com.model.Orders;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class OrderServices {

	
	@Autowired 
	OrderRepository orderRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	//查詢member
	public Member getMember(int userId) {
		
		Optional<Member> optional = memberRepo.findById(userId);
		if(optional.isPresent()) {
			Member memberBean = optional.get();
			return memberBean;
		}
		return null;
	}
	
	//新增訂單明細時尋找最新訂單
	public Orders getLatestOrderByUserIdAndTime(int userId, String localDatetime) {
		
		Optional<Orders> optional = orderRepo.findByUserIdAndOrderTime(userId, localDatetime);
		Orders orders = optional.get();
		return orders;
	}
	
	//新增單筆訂單+明細
	public void saveOrderAndOrderDetails(Orders ordersBean) {
		
		orderRepo.save(ordersBean);
	}
	
	//查詢該會員的所有訂單與訂單明細
	public List<Orders>  findAllOrderByUserId(int userId) {
		
		List<Orders> returnList = orderRepo.findByUserId(userId);
	    
	    // 對訂單列表按照訂單ID降序排序
	    returnList.sort(Comparator.comparingInt(Orders::getOrderId).reversed());
		return returnList;
		
	}
	
}
