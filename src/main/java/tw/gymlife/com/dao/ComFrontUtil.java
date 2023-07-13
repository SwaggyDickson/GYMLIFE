package tw.gymlife.com.dao;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;

import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;

 


@Repository
public interface ComFrontUtil {


	// 將Bean轉成DTO
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList);
	
	//一張圖片的DTO
	public List<CommodityDTO> convertOneCOmPicDTOList(List<Commoditys> comList);
	
	//將bean轉成cartList
	public List<Cart> convertCommodityCartList(List<Commoditys> comList, int comBuyNumber);

	// 將圖片轉檔
	public String convertImageToBase64(byte[] imageBytes);

	// 價錢排序高->低
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList);

	// 價錢排序低->高
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList);
	
	//按下加入購物車後去新增LoadJson檔案
	public List<Cart> loadJson(List<Cart> cartList,int userId);
	
	//進入購物車去讀取Json檔案
	public List<Cart> goIntoCart(int userId);
	
	//刪除購物車內容
	public List<Cart> deleteCart( int userId, int comId);
	
	//轉成訂單DTO
	public List<OrdersDTO> convertOrderToOrdersDTO(List<Orders> orderList, List<CommodityDTO> returnComList);
	
	//發送信件
		
}
