package tw.gymlife.com.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.OrderRepository;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.model.OrdersDetailsDTO;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Service
public class ComOrderService {

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	MemberRepository memberRepo;

	// 查詢member
	public Member getMember(int userId) {

		Optional<Member> optional = memberRepo.findById(userId);
		if (optional.isPresent()) {
			Member memberBean = optional.get();
			return memberBean;
		}
		return null;
	}

	// 新增訂單明細時尋找最新訂單
	public Orders getLatestOrderByUserIdAndTime(int userId, String localDatetime) {

		Optional<Orders> optional = orderRepo.findByUserIdAndOrderTime(userId, localDatetime);
		Orders orders = optional.get();
		return orders;
	}

	// 新增單筆訂單+明細
	public void saveOrderAndOrderDetails(Orders ordersBean) {

		orderRepo.save(ordersBean);
	}

	// 查詢該會員的所有訂單與訂單明細
	public List<Orders> findAllOrderByUserId(int userId) {

		List<Orders> returnList = orderRepo.findByUserId(userId);

		// 對訂單列表按照訂單ID降序排序
		returnList.sort(Comparator.comparingInt(Orders::getOrderId).reversed());
		return returnList;

	}

	// 查詢單筆訂單進金流
	public List<Orders> findOneOrderByOrderId(int orderId) {

		Optional<Orders> option = orderRepo.findById(orderId);
		if (option.isPresent()) {
			Orders order = option.get();
			List<Orders> orderList = new ArrayList<>();
			orderList.add(order);
			return orderList;
		}
		return null;
	}

	// 結帳完成更新狀態
	public void updateOrderCheckStatus(int orderId) {
		Optional<Orders> option = orderRepo.findById(orderId);
		if (option.isPresent()) {
			Orders ordersBean = option.get();
			ordersBean.setOrderPayment(1);
			orderRepo.save(ordersBean);
		}
	}

	// ECPAY
	public String ecpayCheckout(List<OrdersDTO> orderDTOList) {

		AllInOne all = new AllInOne("");

		String orderUUid = "";
		String totalPrice = "";
		int orderId = 0;
		StringBuilder itemNames = new StringBuilder();
		for (OrdersDTO odto : orderDTOList) {
			orderUUid = odto.getOrderUuid();
			totalPrice = Integer.toString(odto.getTotalPrice());
			orderId = odto.getOrderId();
			// 將多個商品名稱合併成字串
			for (OrdersDetailsDTO odetailsdto : odto.getOrderDetailsList()) {
				itemNames.append(odetailsdto.getComName()).append(", ");
			}
			// 移除最后一个逗號和空格
			itemNames.setLength(itemNames.length() - 2);
		}

		// 付款日期
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String formattedDate = sdf.format(date);
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20); // 訂單編號
		// 建立訂單
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate(formattedDate);
		obj.setTotalAmount(totalPrice);
		obj.setTradeDesc("test Description");
		obj.setReturnURL("http://127.0.0.1:4040");
		obj.setClientBackURL("http://localhost:8080/gymlife/payBackorder.func?orderId=" + orderId+"&uuid="+ uuId);
		obj.setNeedExtraPaidInfo("N");
		obj.setItemName(itemNames.toString());

		// 生成綠界表單
		String form = all.aioCheckOut(obj, null);

		return form;
	}

	// 通知，找出所有除了未付款的所有訂單
	public List<Orders> getAllPayedOrders() {

		List<Orders> orderList = orderRepo.findOrdersWithPaymentNotEqualToOneOrderByStatusTime();

		return orderList;
	}

	// 訂單總覽
	public List<Orders> getAllOrders() {

		Sort sort = Sort.by(Sort.Direction.DESC, "orderId");
		List<Orders> allOrderList = orderRepo.findAll(sort);
		return allOrderList;
	}

	//用UUID查找訂單
	public Orders getOrdersByUuid(String uuid) {
		
		Optional<Orders> optional = orderRepo.findByOrderUuid(uuid);
		if(optional.isPresent()) {
			Orders orders = optional.get();
			return orders;
		}
		return null;
	}
	
	//出貨按鈕更改訂單狀態
	public void setOrderStatusByAdmin(Orders orders) {
		
		orderRepo.save(orders);
	}
}
