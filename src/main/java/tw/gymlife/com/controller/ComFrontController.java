package tw.gymlife.com.controller;

import java.lang.runtime.ObjectMethods;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	@GetMapping("/comHome")
	public String getHomePage() {

		return "frontgymlife/com/front_index";
	}

	@GetMapping("/shop")
	public String getShopPage() {

		return "frontgymlife/com/front_shop";
	}

	@GetMapping("/test")
	public String test() {
		return "test/test";
	}

	/*------------shop前導頁功能開始------------*/
	@GetMapping("/type/{comType}")
	public String getShopPageLink(@PathVariable String comType, Model m) {

		List<CommodityDTO> comDTOList = new ArrayList<>();
		try {
			List<Commoditys> returnList = comFService.getComTypeList(comType);
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
	/*------------shop前導頁功能結束------------*/

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
				topThreeComDTOList=comFUtilService.convertOneCOmPicDTOList(topThreeList);
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

		System.out.println("Keyword: " + keywords);
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

		System.out.println("sortBY:" + sortByType);
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

		System.out.println(comId);
		List<CommodityDTO> comDTOList = new ArrayList<>();

		try {
			System.out.println("更新前");
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

			System.out.println(jsonDTOList);
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
	// 假session
	@ModelAttribute("UserId")
	public String fakeSession() {

		return "abc1";
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
//			String virLoc = request.getServletContext().getRealPath("/WEB-INF/resource/IMG");
//		String path = request.getServletContext().getRealPath("/static/gym/com/cart");

			List<CommodityDTO> comDTOList = new ArrayList<>();

			System.out.println("userID: " + userId);
			System.out.println("comId: " + comId);
			System.out.println("數量: " + itemNum);
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

		System.out.println("comID: " + comId);
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
			System.out.println(cartList);

			model.addAttribute("CartList", cartList);
			model.addAttribute("length", cartList.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/getIntoCart.func";
	}

	//購物車按下結帳後生成訂單並跳轉訂單頁面
	@PostMapping("/test.func")
	public String testAjax(@RequestParam("comId") List<Integer> comIds, @RequestParam("totalPrice") String totalPrice,
			@RequestParam("itemNum") List<Integer> itemNums, HttpSession session, Model model) {

		// 生成訂單
		LocalDateTime currentDateTime = LocalDateTime.now(); //現在時間
		//去除小數點
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
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20); //uuid訂單編號
		//取得member做出與訂單一對多
		Member member = orderService.getMember(userId);
		List<Orders> ordersList = new ArrayList<>();
		Orders ordersBean = new Orders(); 
		ordersBean.setOrderPayment(0);  //付款狀態
		ordersBean.setOrderTime(formattedDateTime); //訂單生成日期
		ordersBean.setOrderTotalPrice(totalPriceWithoutDecimal); //總價
		ordersBean.setOrderUuid(uuId); //訂單編號
		ordersBean.setMember(member); //多對一 member
		ordersBean.setUserId(member.getUserId()); // userId
		ordersBean.setOrderStatusTime(formattedDateTime); //訂單狀態時間
		ordersList.add(ordersBean);
		member.setOrders(ordersList);
		memberRepo.save(member); //建立訂單表


		//找出剛剛新增的Order
		Orders latestOrderBean = orderService.getLatestOrderByUserIdAndTime(userId, formattedDateTime);
		System.out.println("剛剛新增的order但還沒有orderDetails: "+latestOrderBean);
		System.out.println("--------------------------訂單建立結束-------------------------------");
		//同時做出與訂單明細的多對一
		List<OrderDetails> orderDetailsList = new ArrayList<>();
		
		//forEach購物車項目
		for (int i = 0; i < comIds.size(); i++) {
			OrderDetails orderDetailsBean= new OrderDetails();
			int comId = comIds.get(i);
			int itemNum = itemNums.get(i);
			orderDetailsBean.setComId(comId); //商品ID
			orderDetailsBean.setPurchaseNumber(itemNum); //商品購買數量
			
			orderDetailsBean.setOrderId(latestOrderBean.getOrderId()); //訂單ID
			orderDetailsBean.setOrders(latestOrderBean); 
			
			orderDetailsList.add(orderDetailsBean);
		}
		latestOrderBean.setOrderDetails(orderDetailsList);
		
		orderService.saveOrderAndOrderDetails(latestOrderBean);
		System.out.println("----------------訂單明細建立結束-----------------------");
		
		//刪除購物車
		for(int i = 0; i < comIds.size(); i++) {
			int comId = comIds.get(i);
			comFUtilService.deleteCart(userId, comId);
		}



		return "redirect:/order.func";
	}

	/*------------shopCart頁功能結束------------*/
	/*------------orderList頁功能開始------------*/
	
	//進入訂單頁面
	@GetMapping("/order.func")
	public String getOrderListByUserId(HttpSession session, Model model) {
		
		int userId;
		if (session.getAttribute("userId") != null) {
			userId = (int) session.getAttribute("userId");
		} else {
			return "redirect:/Login";
		}
		
		List<Orders> returnOrderList = orderService.findAllOrderByUserId(userId);
		System.out.println("該會員的所有訂單: "+ returnOrderList);
		List<Commoditys> returnComList= new ArrayList<>();
		
		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : returnOrderList) {
		    for (OrderDetails odts : orders.getOrderDetails()) {
		        int comId = odts.getComId();
		        // 判斷商品是否已經加入過，如果是則跳過
		        if (addedComIds.contains(comId)) {
		            continue; //跳出迴圈
		        }
		        Commoditys commodity = comFService.getCommoditys(comId);
		        returnComList.add(commodity);
		        addedComIds.add(comId); // 將已經加入的商品ID記錄起來
		    }
		}
		
		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		System.out.println("所有商品表DTO: "+ commodityDTOList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);
		System.out.println("改造後的訂單DTO: "+ ordersDTOList);
		
		model.addAttribute("orderList",ordersDTOList); //往前台傳Lists
		return "frontgymlife/com/front_orderList";
	}
	
	/*------------orderList頁功能結束------------*/

	
	@GetMapping("/payBackorder.func")
	public String payBackOrder(HttpSession session, Model model,@RequestParam("orderId") int orderID) {
		
		int userId= (int) session.getAttribute("userId");
		System.out.println("orderId:"+ orderID);
		orderService.updateOrderCheckStatus(orderID); //更新狀態為已付款

		//更新購買數量
		List<Orders> oneOrderList = orderService.findOneOrderByOrderId(orderID);
		for(Orders orders:oneOrderList ) {
			for(OrderDetails odetails: orders.getOrderDetails()) {
				int comID=odetails.getComId();
				int comBuyNum= odetails.getPurchaseNumber();
				comFService.updateComBuyNumber(comID, comBuyNum);
			}
		}
		
		
		List<Orders> returnOrderList = orderService.findAllOrderByUserId(userId);
		System.out.println("該會員的所有訂單: "+ returnOrderList);
		List<Commoditys> returnComList= new ArrayList<>();
		
		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : returnOrderList) {
		    for (OrderDetails odts : orders.getOrderDetails()) {
		        int comId = odts.getComId();
		        // 判斷商品是否已經加入過，如果是則跳過
		        if (addedComIds.contains(comId)) {
		            continue; //跳出迴圈
		        }
		        Commoditys commodity = comFService.getCommoditys(comId);
		        returnComList.add(commodity);
		        addedComIds.add(comId); // 將已經加入的商品ID記錄起來
		    }
		}
		
		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);
		System.out.println("所有商品表DTO: "+ commodityDTOList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(returnOrderList, commodityDTOList);
		System.out.println("改造後的訂單DTO: "+ ordersDTOList);
		
		model.addAttribute("orderList",ordersDTOList); //往前台傳Lists
		return "frontgymlife/com/front_orderList";
//		return null;
		
	}

}
