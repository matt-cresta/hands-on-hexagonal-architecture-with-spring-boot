package ai.cresta.adapters;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import lombok.Data;

public class genesysSessionHandlerAdapter implements StompSessionHandler {
    private Map<String, Runnable> subscriptionMap = Map.of("channel.metadata", ( ) -> System.out.println("---- Notification Heartbeat ----"));

    public void addSubscriptionCallback(String topic, Runnable callback){
        subscriptionMap.put(topic, callback);
    }

    @Override
    public Type getPayloadType(StompHeaders arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GenericPayload genericPayload = (GenericPayload) payload;
        Runnable callback = subscriptionMap.get(genericPayload.data.topicName);
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("Got an exception");
        exception.printStackTrace();
    }

    @Override
    public void handleTransportError(StompSession arg0, Throwable arg1) {
        // TODO Auto-generated method stub
        
    }

    @Data
    private class GenericPayload {
        private PayloadData data;
    }

    private class PayloadData {
        private String topicName;
    }
}

