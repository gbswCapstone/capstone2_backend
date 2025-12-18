package Capstone.capstoneProject.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;

@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        URI uri = request.getURI();
        String query = uri.getQuery();

        if (query != null && query.contains("token=")) {
            String token = Arrays.stream(query.split("&"))
                    .filter(param -> param.startsWith("token="))
                    .findFirst()
                    .map(param -> param.substring("token=".length()))
                    .orElse(null);

            if (token != null) {
                attributes.put("token", token); // 세션에 저장
            }
        }

        return true; // handshake 진행
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
    }
}
