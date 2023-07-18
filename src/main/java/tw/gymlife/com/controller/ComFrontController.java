package tw.gymlife.com.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.OrderDetails;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.service.ComFrontService;
import tw.gymlife.com.service.ComFrontUtilService;
import tw.gymlife.com.service.ComOrderService;
import tw.gymlife.member.model.Member;
import tw.gymlife.member.model.MemberRepository;

@Controller
public class ComFrontController {

	@Autowired
	private ComFrontService comFService;
	@Autowired
	private ComFrontUtilService comFUtilService;

	@Autowired
	private ComOrderService orderService;

	// Member
	@Autowired
	private MemberRepository memberRepo;

	@GetMapping("/shop")
	public String getShopPage() {

		return "frontgymlife/com/front_shop";
	}

	/*------------shopList頁功能開始------------*/
	// 查詢所有商品
	@GetMapping("/shopList.func")
	public String getShopListPage(Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		List<CommodityDTO> topThreeComDTOList = new ArrayList<>();

		try {
			List<Commoditys> returnList = comFService.getAllComList();
			List<Commoditys> topThreeList = comFService.getTopThreeCommoditys();

			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
				topThreeComDTOList = comFUtilService.convertOneCOmPicDTOList(topThreeList);
			}
			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);
			m.addAttribute("topThreeList", topThreeComDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "frontgymlife/com/front_shopList";
	}

	// 關鍵字查詢
	@GetMapping("/userCheckByKeyWord.func")
	public String getKeywordListPage(Model m, @RequestParam("keywords") String keywords) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getKeywordComList(keywords);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			m.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return "frontgymlife/com/front_shopList";
	}

	// 價格排序
	@GetMapping("/userSortByPrice.func")
	public String getSortByPrice(@RequestParam("sortPrice") String sortByPrice, Model model) {
		List<CommodityDTO> comDTOList = new ArrayList<>();
		List<CommodityDTO> sortList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getSortByPrice();
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
				if (sortByPrice.equals("0")) {
					sortList = comFUtilService.sortByHighPrice(comDTOList);
				} else {
					sortList = comFUtilService.sortByLowPrice(comDTOList);
				}
			}
			// 將資料轉發到商品頁面
			model.addAttribute("comList", sortList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "frontgymlife/com/front_shopList";
	}

	// 類別排序
	@GetMapping("/userSortByType.func")
	public String getSortByType(@RequestParam("sortType") String sortByType, Model model) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getComTypeList(sortByType);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			model.addAttribute("comList", comDTOList);
			// 返回 JSON 数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return "frontgymlife/com/front_shopList";
	}

	/*------------shop單筆商品頁功能開始------------*/
	// 單筆資訊
	@GetMapping("/com/{comId}")
	public String getComDetails(@PathVariable("comId") Integer comId, Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			System.out.println("更新clickTime前:");
			boolean updateOrNot = comFService.updateClickTime(comId);
			System.out.println("更新成功");
			if (updateOrNot) {
				List<Commoditys> returnList = comFService.getOneComList(comId);
				if (returnList != null) {
					comDTOList = comFUtilService.convertCommodityDTOList(returnList);
				}
			}

			// 將資料轉發到商品頁面
			m.addAttribute("comList", comDTOList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "frontgymlife/com/front_oneCom";
	}

	/*------------shop單筆商品頁功能結束------------*/
	// 關鍵字查詢(JS)
	@GetMapping("/userCheckByKeyWord.fun")
	public ResponseEntity<Object> getKeywordListPageJS(Model m, @RequestParam("keywords") String keywords) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getKeywordComList(keywords);
			if (returnList != null) {
				comDTOList = comFUtilService.convertOneCOmPicDTOList(returnList);
			}

			// 將資料轉為JSON轉發到商品頁面
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonDTOList = objectMapper.writeValueAsString(comDTOList);

			// 返回 JSON 数据
			return ResponseEntity.ok(jsonDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 处理异常情况，返回空数据或错误状态码
		return ResponseEntity.notFound().build();
	}

	/*------------shopList頁功能結束------------*/
	/*------------shopCart頁功能開始------------*/

	// 進入購物車
	@GetMapping("/getIntoCart.func")
	public String getUserCart(HttpSession session, Model model) {

		List<Cart> cartList = new ArrayList<>();
		try {
			int userId;
			if (session.getAttribute("userId") != null) {
				userId = (int) session.getAttribute("userId");
				cartList = comFUtilService.goIntoCart(userId);
				if (cartList != null) {
					model.addAttribute("CartList", cartList);
					model.addAttribute("length", cartList.size());
				}
			} else {
				return "redirect:/Login";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "frontgymlife/com/front_shoppingCart";
	}


	// 加入購物車
	@PostMapping("/userAddCart.func")
	public ResponseEntity<Object> getUserAddCart(@RequestParam("comId") int comId,
			@RequestParam("itemNumber") int itemNum, HttpSession session, HttpServletRequest request, Model model) {

		try {
			int userId;
			List<Cart> cartList = new ArrayList<>();
			if (session.getAttribute("userId") != null) {
				userId = (int) session.getAttribute("userId");
			} else {
				return ResponseEntity.notFound().build();
			}

			List<CommodityDTO> comDTOList = new ArrayList<>();
			// 利用此去做事

			List<Commoditys> returnList = comFService.getOneComList(comId);

			if (returnList != null) {
				cartList = comFUtilService.convertCommodityCartList(returnList, itemNum);
				cartList = comFUtilService.loadJson(cartList, userId);

				// 做完寫入後Json後
				System.out.println("已加入購物車");
				comDTOList = comFUtilService.convertCommodityDTOList(returnList);
			}
			// 將資料轉發到商品頁面
			model.addAttribute("OneComList", comDTOList);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	// 刪除購物車商品
	@PostMapping(path = "/getRemoveCart.func")
	public String getRemoveCart(HttpSession session, @RequestParam("comId") int comId, HttpServletRequest request,
			Model model) {

//			String path = request.getServletContext().getRealPath("/WEB-INF/resource/Cart"); //取得路徑
		List<Cart> cartList = new ArrayList<>();

		try {
			int userId;
			if (session.getAttribute("userId") != null) {
				userId = (int) session.getAttribute("userId");
				cartList = comFUtilService.goIntoCart(userId);
				if (cartList != null) {
					model.addAttribute("CartList", cartList);
					model.addAttribute("length", cartList.size());
				}
			} else {
				return "redirect:/Login";
			}
			cartList = comFUtilService.deleteCart(userId, comId);

			model.addAttribute("CartList", cartList);
			model.addAttribute("length", cartList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/getIntoCart.func";
	}

	// 購物車按下結帳後生成訂單並跳轉訂單頁面
	@PostMapping("/getOrder.func")
	public String testAjax(@RequestParam("comId") List<Integer> comIds, @RequestParam("totalPrice") String totalPrice,
			@RequestParam("itemNum") List<Integer> itemNums, HttpSession session, Model model) {

		// 生成訂單
		LocalDateTime currentDateTime = LocalDateTime.now(); // 現在時間
		// 去除小數點
		int totalPriceWithoutDecimal = Integer.parseInt(totalPrice.split("\\.")[0]);

		// 定義日期和時間的格式（不包含毫秒）
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		// 格式化日期和時間
		String formattedDateTime = currentDateTime.format(formatter);
		int userId;
		if (session.getAttribute("userId") != null) {
			userId = (int) session.getAttribute("userId");
		} else {
			return "redirect:/Login";
		}
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20); // uuid訂單編號
		// 取得member做出與訂單一對多
		Member member = orderService.getMember(userId);
		List<Orders> ordersList = new ArrayList<>();
		Orders ordersBean = new Orders();
		ordersBean.setOrderPayment(0); // 付款狀態
		ordersBean.setOrderTime(formattedDateTime); // 訂單生成日期
		ordersBean.setOrderTotalPrice(totalPriceWithoutDecimal); // 總價
		ordersBean.setOrderUuid(uuId); // 訂單編號
		ordersBean.setMember(member); // 多對一 member
		ordersBean.setUserId(member.getUserId()); // userId
		ordersBean.setOrderStatusTime(formattedDateTime); // 訂單狀態時間
		ordersList.add(ordersBean);
		member.setOrders(ordersList);
		memberRepo.save(member); // 建立訂單表
		System.out.println("--------------------------訂單建立結束-------------------------------");

		// 找出剛剛新增的Order
		Orders latestOrderBean = orderService.getLatestOrderByUserIdAndTime(userId, formattedDateTime);
		System.out.println("剛剛新增的order但還沒有orderDetails: " + latestOrderBean);
		// 同時做出與訂單明細的多對一
		List<OrderDetails> orderDetailsList = new ArrayList<>();

		// forEach購物車項目
		for (int i = 0; i < comIds.size(); i++) {
			OrderDetails orderDetailsBean = new OrderDetails();
			int comId = comIds.get(i);
			int itemNum = itemNums.get(i);
			orderDetailsBean.setComId(comId); // 商品ID
			orderDetailsBean.setPurchaseNumber(itemNum); // 商品購買數量

			orderDetailsBean.setOrderId(latestOrderBean.getOrderId()); // 訂單ID
			orderDetailsBean.setOrders(latestOrderBean);

			orderDetailsList.add(orderDetailsBean);
		}
		latestOrderBean.setOrderDetails(orderDetailsList);

		orderService.saveOrderAndOrderDetails(latestOrderBean);
		System.out.println("----------------訂單明細建立結束-----------------------");

		
		// 刪除購物車
		for (int i = 0; i < comIds.size(); i++) {
			int comId = comIds.get(i);
			comFUtilService.deleteCart(userId, comId);
		}
		
		//訂單建立後寄信
		CompletableFuture<Boolean> result = comFUtilService.prepareAndSend("zzxx5576843@gmail.com", userId+"您的訂單已建立成功","訂單編號為: "+latestOrderBean.getOrderUuid());
		System.out.println("信件寄出結果: "+ result);
		
		return "redirect:/order.func";
	}

	/*------------shopCart頁功能結束------------*/
	/*------------orderList頁功能開始------------*/

	// 進入訂單頁面
	@GetMapping("/order.func")
	public String getOrderListByUserId(HttpSession session, Model model) {

		int userId;
		if (session.getAttribute("userId") != null) {
			userId = (int) session.getAttribute("userId");
		} else {
			return "redirect:/Login";
		}

		List<Orders> returnOrderList = orderService.findAllOrderByUserId(userId);
		System.out.println("該會員的所有訂單: " + returnOrderList);
		List<Commoditys> returnComList = new ArrayList<>();

		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : returnOrderList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue; // 跳出迴圈
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);
		System.out.println("該會員的所有訂單DTO: "+ ordersDTOList);

		model.addAttribute("orderList", ordersDTOList); // 往前台傳Lists
		return "frontgymlife/com/front_orderList";
	}

	/*------------orderList頁功能結束------------*/
	
	//綠界call Back函數
	@GetMapping("/payBackorder.func")
	public String getECCallBack(HttpSession session, Model model, @RequestParam("orderId") int orderID,@RequestParam("uuid") String uuid) {
		
		int userId = (int) session.getAttribute("userId");
		orderService.updateOrderCheckStatus(orderID); // 更新狀態為已付款
		
		QueryTradeInfoObj obj = new QueryTradeInfoObj();
		obj.setMerchantTradeNo(uuid);
		AllInOne all = new AllInOne("");
		String result = all.queryTradeInfo(obj);
		
		 // 解析 result 字串並提取 TradeStatus 的值
	    String tradeStatus = getTradeStatusFromResult(result);
		
	    // 根據 TradeStatus 的值進行判斷
	    if (tradeStatus.equals("1")) {
	        System.out.println("交易成功");
	        // 在這裡執行交易成功後的處理
	        
	     // 更新購買數量
			List<Orders> oneOrderList = orderService.findOneOrderByOrderId(orderID);
			for (Orders orders : oneOrderList) {
				for (OrderDetails odetails : orders.getOrderDetails()) {
					int comID = odetails.getComId();
					int comBuyNum = odetails.getPurchaseNumber();
					comFService.updateComBuyNumber(comID, comBuyNum);
					System.out.println("更新數量成功");
				}
			}

			List<Orders> returnOrderList = orderService.findAllOrderByUserId(userId);
			List<Commoditys> returnComList = new ArrayList<>();

			Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
			for (Orders orders : returnOrderList) {
				for (OrderDetails odts : orders.getOrderDetails()) {
					int comId = odts.getComId();
					// 判斷商品是否已經加入過，如果是則跳過
					if (addedComIds.contains(comId)) {
						continue; // 跳出迴圈
					}
					Commoditys commodity = comFService.getCommoditys(comId);
					returnComList.add(commodity);
					addedComIds.add(comId); // 將已經加入的商品ID記錄起來
				}
			}

			List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
			List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);

			model.addAttribute("orderList", ordersDTOList); // 往前台傳Lists
			return "frontgymlife/com/front_orderList";
	    } else if (tradeStatus.equals("0")) {
	        System.out.println("交易失敗");
	        // 在這裡執行交易失敗後的處理
	    } else {
	        System.out.println("交易狀態未知");
	        // 在這裡處理未知狀態的情況
	    }
		
		
//		return null;
		return "frontgymlife/com/front_orderList";
	}
	
	private String getTradeStatusFromResult(String result) {
	    String[] keyValuePairs = result.split("&");
	    for (String pair : keyValuePairs) {
	        String[] keyValue = pair.split("=");
	        if (keyValue.length == 2 && keyValue[0].equals("TradeStatus")) {
	            return keyValue[1];
	        }
	    }
	    return null;
	}

	// LinePAY CALL BACK
	@RequestMapping("/confirm")
	public String handleLinePayConfirm(@RequestParam("transactionId") String transactionId, HttpSession session,
			Model model) throws JSONException {
		// 在这里处理LinePay的confirmURL回调逻辑
		// 解析请求体，处理支付结果等操作

		int userId = (int) session.getAttribute("userId");

		System.out.println("transaction: " + transactionId);
		List<Orders> returnOrderList = orderService.findAllOrderByUserId(userId);
		List<Commoditys> returnComList = new ArrayList<>();

		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : returnOrderList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue; // 跳出迴圈
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);

		model.addAttribute("orderList", ordersDTOList); // 往前台傳Lists
		return "frontgymlife/com/front_orderList";
	}

}
