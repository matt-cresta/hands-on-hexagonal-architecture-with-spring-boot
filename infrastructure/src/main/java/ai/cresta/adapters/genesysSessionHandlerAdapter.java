package ai.cresta.adapters;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import ai.cresta.data.GenesysEventDto;
import ai.cresta.data.GenesysWebChatDto;
import ai.cresta.data.MessageDto;
import ai.cresta.entity.User;
import ai.cresta.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import lombok.Data;

@AllArgsConstructor
public class genesysSessionHandlerAdapter implements StompSessionHandler {

    private final SimpMessagingTemplate messageTemplate;

    private final UserRepository userRepository;

    @Override
    public Type getPayloadType(StompHeaders arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GenesysEventDto genericPayload = (GenesysEventDto) payload;
        switch(genericPayload.getMetaData().getType()){
            case "typing-indicator":
                break;
            case "message":
                GenesysWebChatDto body = (GenesysWebChatDto) genericPayload.getEventBody();
                String conversationId = body.getConversation().getId();
                List<User> users = userRepository.findByConversationId(conversationId);
                User user = users.stream()
                        .filter(userElement -> conversationId.equals(userElement.getConversationId()))
                        .findAny()
                        .orElse(null);
                MessageDto messageDto = MessageDto.builder()
                        .message(body.getBody())
                        .purpose(body.getSender().getRole().toString())
                        .time(DateTime.now().toString())
                        .name(body.getName())
                        .build();
                messageTemplate.convertAndSendToUser(user.getUserId(), "/topic/private-messages", messageDto);
                break;
        }
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

}

