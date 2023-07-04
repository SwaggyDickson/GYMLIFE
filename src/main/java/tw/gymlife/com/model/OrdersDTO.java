package tw.gymlife.com.model;

import java.util.ArrayList;
import java.util.List;

//一筆訂單裡要有的資訊
public class OrdersDTO {

	private int orderId; //訂單編號 
	
	private int userId; // 會員ID

	private String orderTime; // 下單時間

	private String orderPayment; // 付款狀態

	private int totalPrice; // 總價

	private String orderUuid; // 訂單uuid編號
	
	private String orderStatusTime; //訂單狀態時間

	private List<OrdersDetailsDTO> orderDetailsList = new ArrayList<>(); //客製化訂單明細

	public OrdersDTO() {

	}
	
	

	@Override
	public String toString() {
		return "OrdersDTO [orderId=" + orderId + ", userId=" + userId + ", orderTime=" + orderTime + ", orderPayment="
				+ orderPayment + ", totalPrice=" + totalPrice + ", orderUuid=" + orderUuid + ", orderStatusTime="
				+ orderStatusTime + ", orderDetailsList=" + orderDetailsList + "]";
	}



	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderPayment() {
		return orderPayment;
	}

	public void setOrderPayment(String orderPayment) {
		this.orderPayment = orderPayment;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderUuid() {
		return orderUuid;
	}

	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public List<OrdersDetailsDTO> getOrderDetailsList() {
		return orderDetailsList;
	}
	public void setOrderDetailsList(List<OrdersDetailsDTO> orderDetailsList) {
		this.orderDetailsList = orderDetailsList;
	}
	
	public String getOrderStatusTime() {
		return orderStatusTime;
	}
	public void setOrderStatusTime(String orderStatusTime) {
		this.orderStatusTime = orderStatusTime;
	}
}
