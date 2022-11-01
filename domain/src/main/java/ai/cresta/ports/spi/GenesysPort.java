package ai.cresta.ports.spi;

import com.mypurecloud.sdk.v2.model.Channel;

public interface GenesysPort {

    void createNotificationChannel();

    public void subscribeToConversation(String conversationId, Channel channel) ;
}
