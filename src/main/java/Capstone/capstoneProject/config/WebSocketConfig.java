package Capstone.capstoneProject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프론트엔드가 접속할 엔드포인트
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("*") // cors 에러 방지
                .withSockJS();

        // postman test
        registry.addEndpoint("/ws-chat")
                .setAllowedOriginPatterns("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트 -> 서버
        registry.enableSimpleBroker("/sub"); // 서버 -> 클라이언트
    }
}


