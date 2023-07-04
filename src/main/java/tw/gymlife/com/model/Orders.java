package tw.gymlife.com.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.gymlife.member.model.Member;

@Entity
@Table(name = "orders")
@Component("orders")
public class Orders {

	@Id
	@Column(name = "ORDERID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;

	@Column(name = "USERID", insertable = false, updatable = false)
	private int userId;

	@Column(name = "ORDERTIME")
	private String orderTime;

	@Column(name = "ORDERPAYMENT")
	private int orderPayment;  //0=處理中, 1=已付款, 2=商家確認訂單中, 3=訂單已更新, 4=已發貨

	@Column(name = "ORDERTOTALPRICE")
	private int orderTotalPrice;
	
	@Column(name="ORDERUUID")
	private String orderUuid;
	
	@Column(name="ORDERSTATUSTIME")
	private String orderStatusTime;

	// 訂單多對一會員
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USERID")
	private Member members;

	// 訂單一對多訂單明細
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "orders", cascade = CascadeType.ALL)
	private List<OrderDetails> orderDetails = new ArrayList<>();

	public Orders() {

	}

	@Override
	public String toString() {
		return "Orders [orderId=" + orderId + ", userId=" + userId + ", orderTime=" + orderTime + ", orderPayment="
				+ orderPayment + ", orderTotalPrice=" + orderTotalPrice + ", orderUuid=" + orderUuid
				+ ", orderStatusTime=" + orderStatusTime + ", orderDetails=" + orderDetails + "]";
	}

	// --------------------------------------------------------------
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

	public int getOrderPayment() {
		return orderPayment;
	}

	public void setOrderPayment(int orderPayment) {
		this.orderPayment = orderPayment;
	}

	public int getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(int orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	
	public String getOrderUuid() {
		return orderUuid;
	}
	public void setOrderUuid(String orderUuid) {
		this.orderUuid = orderUuid;
	}

	public Member getMember() {
		return members;
	}

	public void setMember(Member member) {
		this.members = member;
	}

	public List<OrderDetails> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	public String getOrderStatusTime() {
		return orderStatusTime;
	}
	public void setOrderStatusTime(String orderStatusTime) {
		this.orderStatusTime = orderStatusTime;
	}

}
