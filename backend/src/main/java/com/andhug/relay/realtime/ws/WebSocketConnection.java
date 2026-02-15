package com.andhug.relay.realtime.ws;

import com.andhug.relay.realtime.registry.Connection;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketConnection extends Connection {

    private WebSocketSession session;

    public WebSocketConnection(WebSocketSession session) {

        this.session = session;
    }

    @Override
    public void sendMessage(Object payload) {

        try {
            this.session.sendMessage(new TextMessage(payload.toString()));
        } catch (Exception e) {
            // do something
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
}
