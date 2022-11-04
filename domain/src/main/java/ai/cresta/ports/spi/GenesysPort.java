package ai.cresta.ports.spi;

import ai.cresta.data.ConversationSubscriptionResponseDto;

public interface GenesysPort {

    public ConversationSubscriptionResponseDto subscribeToConversation(String userId, String conversationId, String topic);
}
