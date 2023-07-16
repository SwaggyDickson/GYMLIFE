package tw.gymlife.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tw.gymlife.com.model.Commoditys;
import tw.gymlife.com.model.LinePayObject;
import tw.gymlife.com.model.OrdersDTO;
import tw.gymlife.com.model.OrdersDetailsDTO;

@Service
public class LinePayService {

	public ResponseEntity<Object> setupLinePay(List<OrdersDTO> comList) throws JSONException {

		// 设置请求的header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Content-Type", "application/json");
		headers.set("X-Line-ChannelId", "2000051801");
		headers.set("X-Line-ChannelSecret", "98483382dcf0e85c09ecb636bc89a5c5");

		System.out.println(comList);
		String mergedProductName = ""; // 用于合并商品名称的字符串
		int totalPrice = 0;
		for (OrdersDTO com : comList) {
			totalPrice = com.getTotalPrice();
			for (OrdersDetailsDTO odto : com.getOrderDetailsList()) {
				// 合并商品名称到mergedProductName
				if (mergedProductName.isEmpty()) {
					mergedProductName = odto.getComName();
				} else {
					mergedProductName += ", " + odto.getComName();
				}
			}
		}

		// 创建LinePay请求的数据对象
		LinePayObject linePayRequest = new LinePayObject(1, "TWD", "MyProd",
				"https://cimg.cnyes.cool/prod/news/4556115/l/79b7b76238dcaa28d626ec007bff576f.jpg",
				"http://localhost:8080/gymlife/confirm", "Order202203110011");
		// 设置请求的data JSON
		JSONObject requestData = new JSONObject();
		requestData.put("amount", totalPrice);
		requestData.put("currency", linePayRequest.getCurrency());
		requestData.put("productName", mergedProductName);
		requestData.put("productImageUrl", linePayRequest.getProductImageUrl());
		requestData.put("confirmUrl", linePayRequest.getConfirmUrl());
		requestData.put("orderId", linePayRequest.getOrderId());
		System.out.println("SuccessData: " + requestData);

		// 发送请求到LinePay API
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> requestEntity = new HttpEntity<>(requestData.toString(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange("https://api-pay.line.me/v2/payments/request",
				HttpMethod.POST, requestEntity, String.class);

		System.out.println("responseEntity: " + responseEntity);
		// 处理LinePay API的响应
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			// 成功接收到响应
			String responseBody = responseEntity.getBody();
			JSONObject responseJson = new JSONObject(responseBody);

			String returnCode = responseJson.getString("returnCode");
			if (returnCode.equals("0000")) {
				JSONObject paymentUrl = responseJson.getJSONObject("info").getJSONObject("paymentUrl");
				String webPaymentUrl = paymentUrl.getString("web");
				String appPaymentUrl = paymentUrl.getString("app");

				System.out.println("returnMSG: " + webPaymentUrl);
				// 返回需要的信息给Controller
				JSONObject response = new JSONObject();
				response.put("webPaymentUrl", webPaymentUrl);
				response.put("appPaymentUrl", appPaymentUrl);
				return ResponseEntity.ok().body(response.toString());
			} else {
				String returnMessage = responseJson.getString("returnMessage");
				// 处理错误情况并返回给Controller
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(returnMessage);
			}
		} else {
			// 请求失败
			return ResponseEntity.status(responseEntity.getStatusCode()).body("Failed to setup LinePay.");
		}
	}
}
