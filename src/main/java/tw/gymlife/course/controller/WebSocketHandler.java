package tw.gymlife.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tw.gymlife.course.model.CorderBean;
import tw.gymlife.course.service.corderService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class WebSocketHandler {
	@Autowired
	private corderService oservice;

    private final SimpMessagingTemplate messagingTemplate;
    

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
  //返回訊息的方法
//    @MessageMapping("/reply/update")
//    @SendTo("/reply/notifications")
//    public String handleNotification(String notification) {
//        // 在这里处理收到的通知消息
//        System.out.println("收到通知：" + notification);
//
//        // 返回响应消息给发送方（可选）
//        return "已收到通知：" + notification+"6666666666";
//    }
    //接收訊息的方法
    @MessageMapping("/updateCorder")
    public void handleUpdateOrder(String message) throws InterruptedException, JsonMappingException, JsonProcessingException {
    	Thread.sleep(1000);
    	ObjectMapper objectmapper = new ObjectMapper();
    	CorderBean messageobj = objectmapper.readValue(message, CorderBean.class);
    	Integer corderId = messageobj.getCorderId();
    	Integer corderQuantity = messageobj.getCorderQuantity();
    	Integer corderCost = messageobj.getCorderCost();
    	System.out.println(corderId);
    	System.out.println(corderQuantity);
    	System.out.println(corderCost);
    	CorderBean obean = oservice.selectCorderBycorderId(corderId);
    	obean.setCorderQuantity(messageobj.getCorderQuantity());
    	obean.setCorderCost(messageobj.getCorderCost());
//    	System.out.println(obean.getCorderId());
//    	System.out.println(obean.getCorderQuantity());
//    	System.out.println(obean.getCorderCost());
        String successmessage = "訂單更新通知: "+message;
        messagingTemplate.convertAndSend("/topic/back", successmessage);
        messagingTemplate.convertAndSend("/topic/back", obean);
    }
    
}
