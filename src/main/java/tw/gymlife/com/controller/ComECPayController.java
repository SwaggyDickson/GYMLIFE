package tw.gymlife.com.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.gymlife.com.model.CommodityDTO;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.OrderDetails;
import tw.gymlife.com.model.Orders;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.service.ComFrontService;
import tw.gymlife.com.service.ComFrontUtilService;
import tw.gymlife.com.service.ComOrderService;

@RestController
public class ComECPayController {

	@Autowired
	ComOrderService orderService;

	@Autowired
	ComFrontService comFService;

	@Autowired
	ComFrontUtilService comFUtilService;

	@PostMapping("/checkoutCom")
	public String checkOutCom(HttpSession session, @RequestParam("payment") String payment,
			@RequestParam("orderId") int orderId, @RequestParam("orderUserId") int userId,
			@RequestParam("orderTime") String orderTime, @RequestParam("orderUuid") String orderUuid,
			@RequestParam("orderTotalPrice") String totalPrice,Model model) {

	    model.addAttribute("merchantId", "2000132");
	    
		if (payment.equals("green")) {
			String ecpayCheckoutFrom = ecpayCheckout(session, payment, orderId, userId, orderTime, orderUuid,
					totalPrice);
			return ecpayCheckoutFrom;
		}

		return null;
	}

	public String ecpayCheckout(HttpSession session, String payment, int orderId, int userId, String orderTime,
			String orderUuid, String totalPrice) {

		List<Orders> orderList = orderService.findOneOrderByOrderId(orderId);
		System.out.println("單筆OrderList:" + orderList);

		List<Commoditys> returnComList = new ArrayList<>();
		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : orderList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue;
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);

		System.out.println("所有商品表DTO: " + commodityDTOList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(orderList, commodityDTOList);

		System.out.println("改造後的訂單DTO: " + ordersDTOList);

		String aioCheckOutALLForm = orderService.ecpayCheckout(ordersDTOList);

		return aioCheckOutALLForm;
	}

	@PostMapping("/testAPI")
	public String test(HttpSession session, @RequestParam("orderId") int orderId, @RequestParam("orderUserId") int userId,
			@RequestParam("orderTime") String orderTime, @RequestParam("orderUuid") String orderUuid,
			@RequestParam("orderTotalPrice") String totalPrice) {

		
		List<Orders> orderList = orderService.findOneOrderByOrderId(orderId);
		System.out.println("單筆OrderList:" + orderList);

		List<Commoditys> returnComList = new ArrayList<>();
		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : orderList) {
			for (OrderDetails odts : orders.getOrderDetails()) {
				int comId = odts.getComId();
				// 判斷商品是否已經加入過，如果是則跳過
				if (addedComIds.contains(comId)) {
					continue;
				}
				Commoditys commodity = comFService.getCommoditys(comId);
				returnComList.add(commodity);
				addedComIds.add(comId); // 將已經加入的商品ID記錄起來
			}
		}

		List<CommodityDTO> commodityDTOList = comFUtilService.convertOneCOmPicDTOList(returnComList);

		System.out.println("所有商品表DTO: " + commodityDTOList);
		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(orderList, commodityDTOList);

		System.out.println("改造後的訂單DTO: " + ordersDTOList);

		String aioCheckOutALLForm = orderService.ecpayCheckout(ordersDTOList);

		return aioCheckOutALLForm;
	}
}
