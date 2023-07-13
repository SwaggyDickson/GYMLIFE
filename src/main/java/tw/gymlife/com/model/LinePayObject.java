package tw.gymlife.com.model;

public class LinePayObject {
	    private int amount;
	    private String currency;
	    private String productName;
	    private String productImageUrl;
	    private String confirmUrl;
	    private String orderId;

	    
	    public LinePayObject() {
	    	
	    }
	    public LinePayObject(int amount, String currency, String productName, String productImageUrl, String confirmUrl, String orderId) {
	        this.amount = amount;
	        this.currency = currency;
	        this.productName = productName;
	        this.productImageUrl = productImageUrl;
	        this.confirmUrl = confirmUrl;
	        this.orderId = orderId;
	    }

	    public int getAmount() {
	        return amount;
	    }

	    public void setAmount(int amount) {
	        this.amount = amount;
	    }

	    public String getCurrency() {
	        return currency;
	    }

	    public void setCurrency(String currency) {
	        this.currency = currency;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public String getProductImageUrl() {
	        return productImageUrl;
	    }

	    public void setProductImageUrl(String productImageUrl) {
	        this.productImageUrl = productImageUrl;
	    }

	    public String getConfirmUrl() {
	        return confirmUrl;
	    }

	    public void setConfirmUrl(String confirmUrl) {
	        this.confirmUrl = confirmUrl;
	    }

	    public String getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(String orderId) {
	        this.orderId = orderId;
	    }

}
