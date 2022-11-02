package ai.cresta.ports.spi;

import ai.cresta.data.ConversationSubscriptionRequestDto;

public interface GenesysPort {

    public void subscribeToConversation(String userId, String conversationId);
}
