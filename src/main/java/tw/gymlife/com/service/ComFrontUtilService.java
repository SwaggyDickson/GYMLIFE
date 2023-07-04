package tw.gymlife.com.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.ComFrontUtil;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;

@Service
public class ComFrontUtilService {

	@Autowired
	private ComFrontUtil userUtil;

	// 將Bean轉成DTO
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList) {
		return userUtil.convertCommodityDTOList(comList);
	}

	// 一張圖片的DTO
	public List<CommodityDTO> convertOneCOmPicDTOList(List<Commoditys> comList) {
		return userUtil.convertOneCOmPicDTOList(comList);
	}

	// 將圖片轉檔
	public String convertImageToBase64(byte[] imgBytes) {
		return userUtil.convertImageToBase64(imgBytes);
	}

	// 價格排序高->低
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList) {
		return userUtil.sortByHighPrice(comDTOList);
	}

	// 價格排序低-> 高
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList) {
		return userUtil.sortByLowPrice(comDTOList);
	}

	// 轉成cartList
	public List<Cart> convertCommodityCartList(List<Commoditys> comList, int comBuyNumber) {
		return userUtil.convertCommodityCartList(comList, comBuyNumber);
	}

	// 按下加入購物車後 LoadJson檔案
	public List<Cart> loadJson(List<Cart> cartList, int userId) {
		return userUtil.loadJson(cartList, userId);
	}

	// 進入購物車去讀取Json檔案
	public List<Cart> goIntoCart(int userId) {
		return userUtil.goIntoCart(userId);
	}

	// 刪除購物車商品按鈕
	public List<Cart> deleteCart(int userId, int comId) {
		return userUtil.deleteCart(userId, comId);
	}

	// 轉成訂單DTO
	public List<OrdersDTO> convertOrderToOrdersDTO(List<Orders> orderList, List<CommodityDTO> returnComList) {

		return userUtil.convertOrderToOrdersDTO(orderList, returnComList);
	}
	
	public CompletableFuture<Boolean> sendMail(List<OrdersDTO> orderDtoList) {
		return userUtil.sendMail(orderDtoList);
	}
}
