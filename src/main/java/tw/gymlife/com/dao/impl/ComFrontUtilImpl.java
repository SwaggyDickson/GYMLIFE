package tw.gymlife.com.dao.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.gymlife.com.dao.ComFrontUtil;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.ComPic;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.OrderDetails;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.model.OrdersDetailsDTO;

@Repository
public class ComFrontUtilImpl implements ComFrontUtil {
	
//	private String pathAll="C:\\GYMLIFEProject\\GYMLIFE\\src\\main\\resources\\static\\gym\\com\\cart";
	private String pathAll="C:\\testGym\\gymproject\\src\\main\\resources\\static\\gym\\com\\cart";
//	private String pathAll = "C:\\springBoot\\workspace\\springBootGym\\src\\main\\resources\\static\\gym\\com\\cart";

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
				comDTO.setClickTime(com.getClickTime());
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

		byte[] aa = "01".getBytes();
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

					comPicInfo.put(comPic.getComPicId(), aa);

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
	public List<Cart> loadJson(List<Cart> cartList, int userId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
		
		existingCartList = null;
		try {
			File file = new File(pathAll + "\\" + userId + ".json");
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
	public List<Cart> goIntoCart(int userId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
//		String path = "C:\\springBoot\\workspace\\springBootGym\\src\\main\\resources\\static\\gym\\com\\cart";
		
		existingCartList = null;
		try {
			File file = new File(pathAll + "\\" + userId + ".json");
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
	public List<Cart> deleteCart(int userId, int comId) {

		ObjectMapper objectMapper = new ObjectMapper();
		List<Cart> existingCartList = new ArrayList<>();
//		String path = "C:\\springBoot\\workspace\\springBootGym\\src\\main\\resources\\static\\gym\\com\\cart";
		
		existingCartList = null;
		try {
			File file = new File(pathAll + "\\" + userId + ".json");
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

	// 生成訂單DTO
	@Override
	public List<OrdersDTO> convertOrderToOrdersDTO(List<Orders> orderList, List<CommodityDTO> returnComList) {

		List<OrdersDTO> comDTOList = new ArrayList<>();
		// 將Hibernate訂單裝入客製化訂單DTO
		for (Orders order : orderList) {
			OrdersDTO ordersDTO = new OrdersDTO();
			ordersDTO.setOrderId(order.getOrderId()); // 訂單ID
			ordersDTO.setUserId(order.getUserId()); // userID
			ordersDTO.setOrderTime(order.getOrderTime()); // 訂單成立時間
			ordersDTO.setOrderStatusTime(order.getOrderStatusTime()); // 訂單狀態時間
			switch (order.getOrderPayment()) {
			case 0:
				ordersDTO.setOrderPayment("處理中");
				break;
			case 1:
				ordersDTO.setOrderPayment("已付款");
				break;
			case 2:
				ordersDTO.setOrderPayment("商家確認訂單中");
				break;
			case 3:
				ordersDTO.setOrderPayment("訂單已更新");
				break;
			case 4:
				ordersDTO.setOrderPayment("已發貨");
				break;
			default:
				ordersDTO.setOrderPayment("未知狀態");
				break;
			}
			ordersDTO.setTotalPrice(order.getOrderTotalPrice()); // 總價
			ordersDTO.setOrderUuid(order.getOrderUuid()); // 訂單的UUID

			List<OrdersDetailsDTO> ordersDetailsDTOsList = new ArrayList<>();
			// 將Hibernate的訂單明細裝入客製化訂單明細+商品明細DTO
			for (OrderDetails ordersDetails : order.getOrderDetails()) {
				OrdersDetailsDTO ordersDetailsDTO = new OrdersDetailsDTO();
				for (CommodityDTO commodityDTO : returnComList) {
					if (ordersDetails.getComId() == commodityDTO.getComId()) {
						ordersDetailsDTO.setComId(commodityDTO.getComId()); // 商品ID
						ordersDetailsDTO.setComName(commodityDTO.getComName()); // 商品名稱
						ordersDetailsDTO.setComPrice(commodityDTO.getComPrice()); // 商品價格
						for (Integer comPicId : commodityDTO.getComPicInfo().keySet()) {
							ordersDetailsDTO.setComPicId(comPicId); // 圖片ID
							break;
						}
						ordersDetailsDTO.setPurchaseNumber(ordersDetails.getPurchaseNumber()); // 購買數量
						ordersDetailsDTOsList.add(ordersDetailsDTO);
					}
				}
			}
			ordersDTO.setOrderDetailsList(ordersDetailsDTOsList);
			comDTOList.add(ordersDTO);
		}

		return comDTOList;
	}

}
