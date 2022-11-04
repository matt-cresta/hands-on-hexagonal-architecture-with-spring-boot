package ai.cresta.service;

import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.spi.GenesysPort;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@AllArgsConstructor
@Component(value="webChat")
public class GenesysWebChatServiceImpl implements MessageServicePort {
    private final GenesysPort genesysPort;

    @Override
    public ConversationSubscriptionResponseDto subscribeToConversation(ConversationSubscriptionRequestDto conversationSubscriptionRequestDto){
        String userId = conversationSubscriptionRequestDto.getUserId();
        String conversationId = conversationSubscriptionRequestDto.getConversationId();
        String topic = MessageFormat.format("v2.conversations.chats.{0}.messages", conversationId);
        return genesysPort.subscribeToConversation(userId, conversationId, topic);
    }
}
