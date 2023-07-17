package tw.gymlife.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketMemberConfig implements WebSocketMessageBrokerConfigurer {
	
	 @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	        registry.addEndpoint("/MemberQuery")
	        
	                .setAllowedOrigins("http://localhost:8080")
	                .withSockJS(); // 启用SockJS支持
	    
	    }
	
//	 
//
//	    @Override
//	    public void configureMessageBroker(MessageBrokerRegistry registry) {
//	        registry.enableSimpleBroker("/topic"); // 配置消息代理（可根据需求进行更改）
//	    }
	 
	 @Override
     public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
         registration.setMessageSizeLimit(10485760) // 設定訊息大小限制
                 .setSendBufferSizeLimit(10485760) // 設定發送緩衝區大小限制
                 .setSendTimeLimit(1000); // 設定發送時間限制
     }
	
}
