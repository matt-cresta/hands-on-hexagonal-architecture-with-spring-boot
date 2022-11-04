package ai.cresta.ports.api;

import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.ConversationSubscriptionResponseDto;

public interface MessageServicePort {

    public ConversationSubscriptionResponseDto subscribeToConversation(ConversationSubscriptionRequestDto conversationSubscriptionRequestDto);
}
