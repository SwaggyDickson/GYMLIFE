package tw.gymlife.com.model;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "orderdetails")
@Component("orderdetails")
public class OrderDetails {

	@Id @Column(name = "ORDERDETAILSID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderDetailsId;
	
	@Column(name = "ORDERID", insertable = false, updatable = false)
	private int orderId;
	
	//fk
	@Column(name = "COMID")
	private int comId;
	
	@Column(name = "PURCHASENUMBER")
	private int purchaseNumber;
	

	
	//訂單明細多對一訂單
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDERID")
	private Orders orders;

	public OrderDetails() {
		
	}
	

	@Override
	public String toString() {
		return "OrderDetails [orderDetailsId=" + orderDetailsId + ", orderId=" + orderId + ", comId=" + comId
				+ ", purchaseNumber=" + purchaseNumber +  "]";
	}



	//--------------------------------------------------------------
	public int getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(int orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getComId() {
		return comId;
	}
	public void setComId(int comId) {
		this.comId = comId;
	}

	public int getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(int purchasNumber) {
		this.purchaseNumber = purchasNumber;
	}

	
	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}
	
	
}
