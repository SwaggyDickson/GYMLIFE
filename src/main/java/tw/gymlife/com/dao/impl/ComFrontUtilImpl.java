package tw.gymlife.com.dao.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Part;
import tw.gymlife.com.dao.ComFrontUtil;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;

@Repository
public class ComFrontUtilImpl implements ComFrontUtil {

	// 將Bean轉成DTO
	@Override
	public List<CommodityDTO> convertCommodityDTOList(List<Commoditys> comList) {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			for (Commoditys com : comList) {
				CommodityDTO comDTO = new CommodityDTO(); // 將取出資訊配合轉成圖片裝進DTO中回傳
				comDTO.setComId(com.getComId()); // id
				comDTO.setComName(com.getComName()); // 商品名稱
				comDTO.setComNumber(com.getComNumber()); // 商品數量
				comDTO.setComPrice(com.getComPrice()); // 商品價格
				comDTO.setComType(com.getComType()); // 商品類別
				comDTO.setComContent(com.getComContent()); // 商品描述
				comDTO.setComStatus(com.getComStatus()); // 商品狀態
				comDTO.setComBuyNumber(com.getComBuyNumber()); // 商品總購買數

				Map<Integer, byte[]> comPicInfo = new HashMap<>();
				for (ComPic comPic : com.getComPics()) {
//					String comPicBase64 = convertImageToBase64(virLocs + "\\" + comPic.getComPicName());
//					String comPicBase64 = convertImageToBase64(comPic.getComPicFile());

					comPicInfo.put(comPic.getComPicId(), comPic.getComPicFile());

					comDTO.setComPicInfo(comPicInfo);
				}
				comDTOList.add(comDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comDTOList;
	}
	
	@Override
	public List<CommodityDTO> convertOneCOmPicDTOList(List<Commoditys> comList) {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			for (Commoditys com : comList) {
				CommodityDTO comDTO = new CommodityDTO(); // 將取出資訊配合轉成圖片裝進DTO中回傳
				comDTO.setComId(com.getComId()); // id
				comDTO.setComName(com.getComName()); // 商品名稱
				comDTO.setComNumber(com.getComNumber()); // 商品數量
				comDTO.setComPrice(com.getComPrice()); // 商品價格
				comDTO.setComType(com.getComType()); // 商品類別
				comDTO.setComContent(com.getComContent()); // 商品描述
				comDTO.setComStatus(com.getComStatus()); // 商品狀態
				comDTO.setComBuyNumber(com.getComBuyNumber()); // 商品總購買數

				Map<Integer, byte[]> comPicInfo = new HashMap<>();
				for (ComPic comPic : com.getComPics()) {
//					String comPicBase64 = convertImageToBase64(virLocs + "\\" + comPic.getComPicName());
//					String comPicBase64 = convertImageToBase64(comPic.getComPicFile());

					comPicInfo.put(comPic.getComPicId(), comPic.getComPicFile());

					comDTO.setComPicInfo(comPicInfo);
					break;
				}
				comDTOList.add(comDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comDTOList;
	}

	// 將圖片轉檔
	@Override
	public String convertImageToBase64(byte[] imageBytes) {

		String base64Image = null;
		try {
			base64Image = Base64.getEncoder().encodeToString(imageBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64Image;
	}

	// 價格排序高->低
	@Override
	public List<CommodityDTO> sortByHighPrice(List<CommodityDTO> comDTOList) {

		Comparator<CommodityDTO> comparator = new Comparator<CommodityDTO>() {

			@Override
			public int compare(CommodityDTO o1, CommodityDTO o2) {

				return Integer.compare(o2.getComPrice(), o1.getComPrice());
				// 價格高的排前面，使用 o2 和 o1 互換位置
			}
		};
		// 使用 Collections.sort() 方法進行排序
		Collections.sort(comDTOList, comparator);
		// 將取得的商品全部傳入一個標籤裡面
		return comDTOList;
	}

	// 價格排序低-> 高
	@Override
	public List<CommodityDTO> sortByLowPrice(List<CommodityDTO> comDTOList) {

		Comparator<CommodityDTO> comparator = new Comparator<CommodityDTO>() {

			@Override
			public int compare(CommodityDTO o1, CommodityDTO o2) {

				return Integer.compare(o1.getComPrice(), o2.getComPrice());
				// 價格低的排前面，使用 o1 和 o2 互換位置
			}
		};
		// 使用 Collections.sort() 方法進行排序
		Collections.sort(comDTOList, comparator);
		return comDTOList;
	}

	// 轉成cartList
	@Override
	public List<Cart> convertCommodityCartList(List<Commoditys> comList, int comBuyNumber) {

		List<Cart> carList = new ArrayList<>();
		String comPicName = null;
		try {
			for (Commoditys com : comList) {
				Cart cartDTO = new Cart(); // 將取出資訊配合轉成圖片裝進DTO中回傳
				cartDTO.setComId(com.getComId()); // id
				cartDTO.setComName(com.getComName()); // 商品名稱
				cartDTO.setComNumber(comBuyNumber); // !!!!變成商品購買數量!!!
				cartDTO.setComPrice(com.getComPrice()); // 商品價格
				cartDTO.setComType(com.getComType()); // 商品類別
				cartDTO.setComContent(com.getComContent()); // 商品描述
				cartDTO.setComStatus(com.getComStatus()); // 商品狀態
				cartDTO.setComBuyNumber(com.getComBuyNumber()); // 商品總購買數

				Map<Integer, byte[]> comPicInfo = new HashMap<>();
				for (ComPic comPic : com.getComPics()) {
					comPicName = comPic.getComPicName();
//					String comPicBase64 = convertImageToBase64(comPic.getComPicFile());
					
					
//					cartDTO.setComPicBase64(comPicBase64); // 圖片
					comPicInfo.put(comPic.getComPicId(), comPic.getComPicFile());

					cartDTO.setComPicInfo(comPicInfo);
					break; // 停止迴圈以取得第一筆資料
				}
//				String comPicBase64 = convertImageToBase64(virLocs + "\\" + comPicName);
				carList.add(cartDTO);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return carList;
	}

	// 按下加入購物車後 LoadJson檔案
	@Override
	public List<Cart> loadJson(List<Cart> cartList, String path, String userId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
		existingCartList = null;
		try {
			File file = new File(path + "\\" + userId + ".json");
			if (file.exists()) {
				existingCartList = objectMapper.readValue(file, new TypeReference<List<Cart>>() {
				});

				// 比對現有的資料與新的資料
				boolean isMatched = false;
				for (Cart cart : existingCartList) {
					if (cartList.stream().anyMatch(c -> c.getComId() == cart.getComId())) {
						isMatched = true;
						break;
					}
				}

				if (isMatched) {
					// 如果資料匹配，回傳原有的 JSON 檔案中的資料
					return existingCartList;
				} else {
					// 否則，將新的資料加入現有的列表
					existingCartList.addAll(cartList);
					objectMapper.writeValue(file, existingCartList); // 寫入
				}
			} else {
				// 如果檔案不存在，直接將 cartList 寫入新的 JSON 檔案
				objectMapper.writeValue(file, cartList);
				existingCartList = objectMapper.readValue(file, new TypeReference<List<Cart>>() {
				});// 寫入後讀取並回傳
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return existingCartList;
	}

	// 進入購物車去讀取Json檔案
	@Override
	public List<Cart> goIntoCart(String path, String userId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
		existingCartList = null;
		try {
			File file = new File(path + "\\" + userId + ".json");
			if (file.exists()) {
				existingCartList = objectMapper.readValue(file, new TypeReference<List<Cart>>() {
				});
				return existingCartList;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return existingCartList;
	}

	// 刪除購物車商品按鈕
	@Override
	public List<Cart> deleteCart(String path, String userId, int comId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
		existingCartList = null;
		try {
			File file = new File(path + "\\" + userId + ".json");
			if (file.exists()) {
				existingCartList = objectMapper.readValue(file, new TypeReference<List<Cart>>() {
				}); // 讀取json檔案並轉成beanList

				// 比對商品 ID，找到對應的購物車項目並刪除
				for (int i = 0; i < existingCartList.size(); i++) {
					Cart cart = existingCartList.get(i);
					if (cart.getComId() == comId) {
						existingCartList.remove(i);
						break;
					}
				}

				// 重新寫入 JSON 檔案
				objectMapper.writeValue(file, existingCartList);

				// 回傳更新後的購物車列表
				return existingCartList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
