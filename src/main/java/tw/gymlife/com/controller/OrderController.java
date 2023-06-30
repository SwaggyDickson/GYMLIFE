package tw.gymlife.com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import tw.gymlife.com.model.Cart;
import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.service.ComFrontService;
import tw.gymlife.com.service.ComFrontUtilService;

@RestController
public class OrderController {

	@Autowired
	private ComFrontService comFService;
	@Autowired
	private ComFrontUtilService comFUtilService;
	@Autowired
	OrderService orderService;

	@PostMapping("/ecpayCheckoutCom")
	public String ecpayCheckout(HttpSession session,@RequestParam("payment") String payment, @RequestParam("orderId") String orderId, @RequestParam("orderUserId") int userId,
			@RequestParam("orderTime") String orderTime, @RequestParam("orderUuid") String orderUuid, @RequestParam("orderTotalPrice") int totalPrice) {
		
		
		System.out.println("orderId:" + orderId);
		System.out.println("orderUserId:" + userId);
		System.out.println("orderTime:" + orderTime);
		System.out.println("orderUuid:" + orderUuid);
		System.out.println("orderTotalPrice:" + totalPrice);
		
		
//		//去除小數點
//		String totalPriceWithoutDecimal = totalPrice.split("\\.")[0];
//
//		List<Cart> cartList = new ArrayList<>();
//		for (int i = 0; i < comIds.size(); i++) {
//			int comId = comIds.get(i);
//			int itemNum = itemNums.get(i);
//			
//			//利用id去生成購物車list
//			List<Commoditys> returnList = comFService.getOneComList(comId);
//			List<Cart> convertedCartList = comFUtilService.convertCommodityCartList(returnList, itemNum);
//
//			
//			cartList.addAll(convertedCartList);
//		}
//		String aioCheckOutALLForm = orderService.ecpayCheckout(cartList,totalPriceWithoutDecimal);
		
		return null;
	}

}
