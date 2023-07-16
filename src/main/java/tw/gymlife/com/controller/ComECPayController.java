package tw.gymlife.com.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.ResponseEntity;
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
import tw.gymlife.com.service.LinePayService;

@RestController
public class ComECPayController {

	@Autowired
	ComOrderService orderService;

	@Autowired
	ComFrontService comFService;

	@Autowired
	ComFrontUtilService comFUtilService;
	
	@Autowired
	private LinePayService linePayService;

	@PostMapping("/checkoutCom")
	public String checkOutCom(HttpSession session, @RequestParam("payment") String payment,
			@RequestParam("orderId") int orderId, @RequestParam("orderUserId") int userId,
			@RequestParam("orderTime") String orderTime, @RequestParam("orderUuid") String orderUuid,
			@RequestParam("orderTotalPrice") String totalPrice, Model model) {


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

		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(orderList, commodityDTOList);


		String aioCheckOutALLForm = orderService.ecpayCheckout(ordersDTOList);

		return aioCheckOutALLForm;
	}


	@PostMapping("/getLinePay")
	public ResponseEntity<Object> getLinePay(HttpSession session,@RequestParam("orderId") int orderId) throws JSONException {

		orderService.updateOrderCheckStatus(orderId); //更新狀態為已付款

		//更新購買數量
		List<Orders> oneOrderList = orderService.findOneOrderByOrderId(orderId);
		for(Orders orders:oneOrderList ) {
			for(OrderDetails odetails: orders.getOrderDetails()) {
				int comID=odetails.getComId();
				int comBuyNum= odetails.getPurchaseNumber();
				comFService.updateComBuyNumber(comID, comBuyNum);
			}
		}
		List<Commoditys> returnComList = new ArrayList<>();
		Set<Integer> addedComIds = new HashSet<>(); // 用來記錄已經加入的商品ID
		for (Orders orders : oneOrderList) {
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

		List<OrdersDTO> ordersDTOList = comFUtilService.convertOrderToOrdersDTO(oneOrderList, commodityDTOList);
		
		return linePayService.setupLinePay(ordersDTOList);
	}
	

}
