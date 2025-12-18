package Capstone.capstoneProject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;
    private final HttpHandshakeInterceptor httpHandshakeInterceptor;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .addInterceptors(httpHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub"); // 클라이언트 -> 서버
        registry.enableSimpleBroker("/sub"); // 서버 -> 클라이언트
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        StompHandler stompHandler = applicationContext.getBean(StompHandler.class);
        registration.interceptors(jwtChannelInterceptor, stompHandler);
    }

}


