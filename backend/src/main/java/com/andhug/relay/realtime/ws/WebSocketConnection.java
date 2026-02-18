package com.andhug.relay.realtime.ws;

import com.andhug.relay.realtime.registry.Connection;
import com.andhug.relay.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
public class WebSocketConnection extends Connection {

    private WebSocketSession session;

    public WebSocketConnection(WebSocketSession session) {

        this.session = session;
    }

    @Override
    public void sendMessage(Object payload) {

        try {
            this.session.sendMessage(new TextMessage(toJson(payload)));
        } catch (Exception e) {
            // do something
            log.info("Error sending message to {}, {}", payload, e.getMessage());
        }
    }

    @Override
    public boolean isActive() {

        return this.session.isOpen();
    }

    @Override
    public void close() {

        try {
            this.session.close();
        } catch (Exception e) {
            // do something
        }
    }

    private String toJson(Object object) throws JsonProcessingException {
        return JsonUtils.toJson(object);
    }
}
