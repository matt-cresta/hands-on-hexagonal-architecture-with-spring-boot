package ai.cresta.adapters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ai.cresta.data.ConversationSubscriptionRequestDto;
import ai.cresta.data.UserDto;
import ai.cresta.entity.User;
import ai.cresta.repository.BookRepository;
import ai.cresta.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.model.Channel;
import com.mypurecloud.sdk.v2.model.ChannelTopic;
import com.mypurecloud.sdk.v2.model.ChannelTopicEntityListing;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.api.NotificationsApi;

import ai.cresta.ports.spi.GenesysPort;
@Service
public class GenesysAdapter implements GenesysPort {
    private final PureCloudRegionHosts region = PureCloudRegionHosts.us_west_2;

    @Autowired
    private UserRepository userRepository;

    private Channel createNotificationChannelForUser(NotificationsApi notificationsApi) {
        //Set up the web-socket
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new genesysSessionHandlerAdapter();


        Channel channel = null;

        try{
            channel = notificationsApi.postNotificationsChannels();
            String connectUri = channel.getConnectUri();

            stompClient.connect(connectUri, sessionHandler);
            new Scanner(System.in).nextLine(); // Don't close immediately.

        }
        catch(ApiException e){
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannels");
            e.printStackTrace();
        }
        catch(IOException e){
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannels, Request failed to process");
        }
        return channel;
    }

    private void configureAPI(String userId){
        List<User> users =  userRepository.findByUserId(userId);

        User user = users.stream()
                .filter(userElement -> userId.equals(userElement.getUserId()))
                .findAny()
                .orElse(null);

        if(user == null)
            return;

        ApiClient apiClient = ApiClient.Builder.standard().withBasePath(region).build();
        apiClient.setAccessToken(user.getAccessToken());

        Configuration.setDefaultApiClient(apiClient);
    }

    @Override
    public void subscribeToConversation(String userId, String conversationId) {
        configureAPI(userId);
        NotificationsApi notificationsApi = new NotificationsApi();

        Channel channel = createNotificationChannelForUser(notificationsApi);
        String channelId = channel.getId();

        ChannelTopic channelTopic = new ChannelTopic().id(channelId);
        List<ChannelTopic> body = Collections.singletonList(channelTopic);

        try {
            // Add a list of subscriptions to the existing list of subscriptions
            ChannelTopicEntityListing result = notificationsApi.postNotificationsChannelSubscriptions(channelId, body);
            System.out.println(result);
        }
        catch (ApiException e) {
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannelSubscriptions");
            e.printStackTrace();
        }
        catch(IOException e){
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannels, Request failed to process");
        }
    }
}
