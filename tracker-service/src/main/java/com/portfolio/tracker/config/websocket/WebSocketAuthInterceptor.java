package com.portfolio.tracker.config.websocket;

import com.portfolio.tracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() != StompCommand.CONNECT) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MessageDeliveryException("Unauthorized");
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            if (!jwtUtil.isTokenValid(token, username)) {
                throw new MessageDeliveryException("Unauthorized");
            }
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());
            accessor.setUser(auth);
            log.info("WebSocket CONNECT authenticated: {}", username);
        } catch (MessageDeliveryException e) {
            throw e;
        } catch (Exception e) {
            throw new MessageDeliveryException("Unauthorized");
        }

        return message;
    }
}
