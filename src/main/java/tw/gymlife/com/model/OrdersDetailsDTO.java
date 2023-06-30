package tw.gymlife.com.model;

public class OrdersDetailsDTO {


	private int comId; // 商品ID

	private String comName; // 商品名稱

	private int comPrice; // 商品價格

	private int purchaseNumber; // 購買數量

	private int comPicId; // 商品圖片ID

	public OrdersDetailsDTO() {

	}

	@Override
	public String toString() {
		return "OrdersDetailsDTO [comId=" + comId + ", comName=" + comName + ", comPrice="
				+ comPrice + ", purchaseNumber=" + purchaseNumber + ", comPicId=" + comPicId + "]";
	}

	//------------------------------------------------------------------------


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

	public int getComPrice() {
		return comPrice;
	}

	public void setComPrice(int comPrice) {
		this.comPrice = comPrice;
	}

	public int getPurchaseNumber() {
		return purchaseNumber;
	}

	public void setPurchaseNumber(int purchaseNumber) {
		this.purchaseNumber = purchaseNumber;
	}

	public int getComPicId() {
		return comPicId;
	}

	public void setComPicId(int comPicId) {
		this.comPicId = comPicId;
	}
}
