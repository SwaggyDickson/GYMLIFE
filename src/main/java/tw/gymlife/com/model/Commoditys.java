package tw.gymlife.com.model;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity @Table(name = "commoditys")
@Component("commoditys")
public class Commoditys {

	@Id @Column(name = "COMID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int comId; // id

	@Column(name = "COMNAME")
	private String comName; // 商品名稱

	@Column(name = "COMNUMBER")
	private int comNumber; // 商品數量

	@Column(name = "COMPRICE")
	private int comPrice; // 商品價格

	@Column(name = "COMCONTENT")
	private String comContent; // 商品描述

	@Column(name = "COMSTATUS")
	private String comStatus; // 商品狀態(上架、下架)

	@Column(name = "COMTYPE")
	private String comType; // 商品類型

	@Column(name = "COMBUYNUMBER")
	private int comBuyNumber; // 購買此商品總量
	
	@Column(name = "CLICKTIME")
	private int clickTime;

//	@Column(name = "COMPICNAME")
//	private String comPicName;  //商品圖片名稱

	// 商品圖片一對多
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "commoditys", cascade = CascadeType.ALL)
	private Set<ComPic> comPics = new LinkedHashSet<>();

	public Commoditys() {
	}

//	public Commoditys(int comId, String comName, int comNumber, int comPrice, String comContent, String comStatus,
//			String comType, int comBuyNumber, String comPicName) {
//		this.comId = comId;
//		this.comName = comName;
//		this.comNumber = comNumber;
//		this.comPrice = comPrice;
//		this.comContent = comContent;
//		this.comStatus = comStatus;
//		this.comType = comType;
//		this.comBuyNumber = comBuyNumber;
//		this.comPicName= comPicName;
//	}

	// @Override
//	public String toString() {
//		return "Commoditys [comId=" + comId + ", comName=" + comName + ", comNumber=" + comNumber + ", comPrice="
//				+ comPrice + ", comContent=" + comContent + ", comStatus=" + comStatus + ", comType=" + comType
//				+ ", comBuyNumber=" + comBuyNumber + ", comPicName=" + comPicName + "]";
//	}
//	public String getComPicName() {
//		return comPicName;
//	}
//	public void setComPicName(String comPicName) {
//		this.comPicName = comPicName;
//	}
	
	public Commoditys(int comId, String comName, int comNumber, int comPrice, String comContent, String comStatus,
			String comType, int comBuyNumber, Set<ComPic> comPics, int clickTime) {
		super();
		this.comId = comId;
		this.comName = comName;
		this.comNumber = comNumber;
		this.comPrice = comPrice;
		this.comContent = comContent;
		this.comStatus = comStatus;
		this.comType = comType;
		this.comBuyNumber = comBuyNumber;
		this.comPics = comPics;
		this.clickTime= clickTime;
	}

	@Override
	public String toString() {
		return "Commoditys [comId=" + comId + ", comName=" + comName + ", comNumber=" + comNumber + ", comPrice="
				+ comPrice + ", comContent=" + comContent + ", comStatus=" + comStatus + ", comType=" + comType
				+ ", comBuyNumber=" + comBuyNumber + ", comPics=" + comPics + "]";
	}

	public Set<ComPic> getComPics() {
		return comPics;
	}

	public void setComPics(Set<ComPic> comPics) {
		this.comPics = comPics;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public int getComNumber() {
		return comNumber;
	}

	public void setComNumber(int comNumber) {
		this.comNumber = comNumber;
	}

	public int getComPrice() {
		return comPrice;
	}

	public void setComPrice(int comPrice) {
		this.comPrice = comPrice;
	}

	public String getComContent() {
		return comContent;
	}

	public void setComContent(String comContent) {
		this.comContent = comContent;
	}

	public String getComStatus() {
		return comStatus;
	}

	public void setComStatus(String comStatus) {
		this.comStatus = comStatus;
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public int getComBuyNumber() {
		return comBuyNumber;
	}

	public void setComBuyNumber(int comBuyNumber) {
		this.comBuyNumber = comBuyNumber;
	}
	
	public int getClickTime() {
		return clickTime;
	}
	public void setClickTime(int clickTime) {
		this.clickTime = clickTime;
	}
	
}
