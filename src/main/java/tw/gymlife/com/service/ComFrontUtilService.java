package tw.gymlife.com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.gymlife.com.dao.ComFrontUtil;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;

@Service
public class ComFrontUtilService {

	@Autowired
	private ComFrontUtil userUtil;

	// 將Bean轉成DTO
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList) {
		return userUtil.convertCommodityDTOList(comList);
	}
	//一張圖片的DTO
	public List<CommodityDTO> convertOneCOmPicDTOList(List<Commoditys> comList){
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
	public List<Cart> loadJson(List<Cart> cartList, String path, String userId) {
		return userUtil.loadJson(cartList, path, userId);
	}

	// 進入購物車去讀取Json檔案
	public List<Cart> goIntoCart(String path, String userId) {
		return userUtil.goIntoCart(path, userId);
	}

	// 刪除購物車商品按鈕
	public List<Cart> deleteCart(String path, String userId, int comId) {
		return userUtil.deleteCart(path, userId, comId);
	}
}
