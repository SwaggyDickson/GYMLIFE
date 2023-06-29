package tw.gymlife.com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommodityDTO {

	private int comId;  //id
	private String comName;  //商品名稱
	private int comNumber;  //商品數量
	private int comPrice;  //商品價格
	private String comContent;  //商品描述
	private String comStatus;  //商品狀態(上架、下架)
	private String comType;  //商品類型
	private int comBuyNumber;  //購買此商品總量
	private String comPicBase64;  //商品圖片名稱
	private List<String> comPicBase64List =new ArrayList<>();	
	private Map<Integer, byte[]> comPicInfo = new HashMap<>();
	
	
	
//	@Override
//	public String toString() {
//		return "CommoddityDTO [comId=" + comId + ", comName=" + comName + ", comNumber=" + comNumber + ", comPrice="
//				+ comPrice + ", comContent=" + comContent + ", comStatus=" + comStatus + ", comType=" + comType
//				+ ", comBuyNumber=" + comBuyNumber + ", comPicName=" + comPicBase64 + "]";
//	}
	
//	@Override
//	public String toString() {
//		return "CommodityDTO [comId=" + comId + ", comName=" + comName + ", comNumber=" + comNumber + ", comPrice="
//				+ comPrice + ", comContent=" + comContent + ", comStatus=" + comStatus + ", comType=" + comType
//				+ ", comBuyNumber=" + comBuyNumber + ", comPicBase64List=" + comPicBase64List + "]";
//	}

	@Override
	public String toString() {
		return "CommodityDTO [comId=" + comId + ", comName=" + comName + ", comNumber=" + comNumber + ", comPrice="
				+ comPrice + ", comContent=" + comContent + ", comStatus=" + comStatus + ", comType=" + comType
				+ ", comBuyNumber=" + comBuyNumber + ", comPicInfo=" + comPicInfo + "]";
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
	public String getComPicBase64() {
		return comPicBase64;
	}
	public void setComPicBase64(String comPicName) {
		this.comPicBase64 = comPicName;
	}

	public List<String> getComPicBase64List() {
		return comPicBase64List;
	}
	public void setComPicBase64List(List<String> comPicBase64List) {
		this.comPicBase64List = comPicBase64List;
	}
	
	public Map<Integer, byte[]> getComPicInfo() {
		return comPicInfo;
	}
	public void setComPicInfo(Map<Integer, byte[]> comPicInfo) {
		this.comPicInfo = comPicInfo;
	}
}
