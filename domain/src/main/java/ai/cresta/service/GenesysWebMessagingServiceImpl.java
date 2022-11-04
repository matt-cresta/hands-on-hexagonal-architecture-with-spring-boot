package ai.cresta.service;

import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.spi.GenesysPort;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@AllArgsConstructor
@Component(value="webMessaging")
public class GenesysWebMessagingServiceImpl implements MessageServicePort {

    private final SimpMessagingTemplate messageTemplate;
    private final GenesysPort genesysPort;
    
    @Override
    public ConversationSubscriptionResponseDto subscribeToConversation(ConversationSubscriptionRequestDto conversationSubscriptionRequestDto){
        String userId = conversationSubscriptionRequestDto.getUserId();
        String conversationId = conversationSubscriptionRequestDto.getConversationId();
        String topic = MessageFormat.format("v2.users.{0}.conversations.messages", userId);
        return genesysPort.subscribeToConversation(userId, conversationId, topic);
    }
}
