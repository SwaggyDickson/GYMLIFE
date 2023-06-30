package tw.gymlife.com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tw.gymlife.com.model.Cart;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

@Service
public class OrderService {

	 public String ecpayCheckout(List<Cart> cartList, String totalPrice) {
	        AllInOne all = new AllInOne("");
	        
	        // 將多個商品名稱合併成字串
	        StringBuilder itemNames = new StringBuilder();
	        for (Cart cart : cartList) {
	            itemNames.append(cart.getComName()).append(", ");
	        }
	        // 移除最后一个逗號和空格
	        itemNames.setLength(itemNames.length() - 2);

	        // 建立訂單
	        String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20); //訂單編號
	        AioCheckOutALL obj = new AioCheckOutALL();
	        obj.setMerchantTradeNo(uuId);
	        obj.setMerchantTradeDate("2017/01/01 08:05:23");
	        obj.setTotalAmount(totalPrice);
	        obj.setTradeDesc("test Description");
	        obj.setReturnURL("http://localhost:8080/gymlife/callback");
	        obj.setNeedExtraPaidInfo("N");
	        obj.setItemName(itemNames.toString());

	        // 生成綠界表單
	        String form = all.aioCheckOut(obj, null);

	        return form;
	}
}