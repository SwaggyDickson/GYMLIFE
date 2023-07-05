package tw.gymlife.course.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/updateOrder")
    public void handleUpdateOrder(String message) {
        // 处理从前端发送的消息
        // 执行相应的更新订单操作

        // 发送通知给后台
        String notification = "收到新订单：" + message;
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
}
