//package tw.gymlife.course.controller;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Controller;
////返回的Controller
//@Controller
//public class NotificationController {
//
//    @MessageMapping("/reply/update")
//    @SendTo("/reply/notifications") // 可选，用于广播消息给所有订阅该目的地的客户端
//    public String handleNotification(String notification) {
//        // 在这里处理收到的通知消息
//        System.out.println("收到通知：" + notification);
//        
//        // 返回响应消息给发送方（可选）
//        return "已收到通知：" + notification;
//    }
//}
//
