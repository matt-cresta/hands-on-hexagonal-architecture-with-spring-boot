package ai.cresta.ports.api;

import ai.cresta.data.ConversationSubscriptionRequestDto;

public interface MessageServicePort {

    public void notifyFrontend(final String messages);

    public void notifyUser(final String id, final String messages);

    public void subscribeToConversation(ConversationSubscriptionRequestDto conversationSubscriptionRequestDto);
}
