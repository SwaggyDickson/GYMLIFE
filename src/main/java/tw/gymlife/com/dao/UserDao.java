package tw.gymlife.com.dao;

import java.util.List;

import tw.gymlife.com.model.Commoditys;





public interface UserDao {


	//商品列表
	
	/**
	 * 查詢該賣家的商品方法
	 * 回傳list型態
	 */
	public List<Commoditys> inquireAllCommodity();
	
	//修改按鈕

	/**
	 * 修改前 先查詢該ID之方法
	 */
	public List<Commoditys> inquireOneCommodity(int id);
	

	//Aside功能

	/**
	 * 關鍵字 查詢方法 
	 */
	public List<Commoditys> searchByName(String comName);
	
	/**
	 * 價格排序方法 
	 */
	public List<Commoditys> searchByPrice();
	/**
	 * 搜尋類型方法
	 */
	public List<Commoditys> searchByType(String comType);
	//加入購物車
	public List<Commoditys> addCart(int id,int itemNum);
}
