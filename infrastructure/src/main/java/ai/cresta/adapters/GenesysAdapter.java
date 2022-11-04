package ai.cresta.adapters;

import ai.cresta.data.ConversationSubscriptionResponseDto;
import ai.cresta.data.GenesysEventDto;
import ai.cresta.data.GenesysWebChatDto;
import ai.cresta.data.MessageDto;
import ai.cresta.entity.User;
import ai.cresta.repository.UserRepository;

import com.mypurecloud.sdk.v2.api.ConversationsApi;
import com.mypurecloud.sdk.v2.model.*;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.api.NotificationsApi;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import ai.cresta.ports.spi.GenesysPort;
@Service
public class GenesysAdapter implements GenesysPort {
    private final PureCloudRegionHosts region = PureCloudRegionHosts.us_west_2;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ConversationSubscriptionResponseDto subscribeToConversation(String userId, String conversationId, String topic) {
        User user = configureAPI(userId);
        NotificationsApi notificationsApi = new NotificationsApi();
        ConversationsApi conversationsApi = new ConversationsApi();
        String channelId;

        if(user != null && user.getChannelId() != null){
            channelId = user.getChannelId();
        }
        else{
            Channel channel = createNotificationChannelForUser(notificationsApi);
            channelId = channel.getId();
            user.setChannelId(channelId);
            userRepository.save(user);
        }

        ChannelTopic channelTopic = new ChannelTopic().id(topic);
        List<ChannelTopic> body = Collections.singletonList(channelTopic);

        try {
            // Add a list of subscriptions to the existing list of subscriptions
            ChannelTopicEntityListing result = notificationsApi.postNotificationsChannelSubscriptions(channelId, body);
            return getParticpantInfo(conversationsApi, conversationId);
        }
        catch (ApiException e) {
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannelSubscriptions");
            e.printStackTrace();
        }
        catch(IOException e){
            System.err.println("Exception when calling NotificationsApi#postNotificationsChannels, Request failed to process");
        }
        return null;
    }

    private User configureAPI(String userId){
        List<User> users =  userRepository.findByUserId(userId);

        User user = users.stream()
                .filter(userElement -> userId.equals(userElement.getUserId()))
                .findAny()
                .orElse(null);

        if(user == null)
            return null;

        ApiClient apiClient = ApiClient.Builder.standard().withBasePath(region).build();
        apiClient.setAccessToken(user.getAccessToken());

        Configuration.setDefaultApiClient(apiClient);

        return user;
    }

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

    private ConversationSubscriptionResponseDto getParticpantInfo(ConversationsApi conversationsApi, String conversationId){
        try{
            Conversation conversation = conversationsApi.getConversation(conversationId);
            Participant agent = conversation.getParticipants()
                    .stream()
                    .filter(participant -> participant.getPurpose().equals("agent"))
                    .findAny()
                    .orElse(null);

            Participant customer = conversation.getParticipants()
                    .stream()
                    .filter(participant -> participant.getPurpose().equals("customer"))
                    .findAny()
                    .orElse(null);

            return ConversationSubscriptionResponseDto.builder()
                    .agent(agent)
                    .customer(customer)
                    .build();
        }
        catch (ApiException e) {
            System.err.println("Exception when calling ConversationsApi#getConversation");
            e.printStackTrace();
        }
        catch(IOException e){
            System.err.println("Exception when calling ConversationsApi#getConversation, Request failed to process");
        }
        return null;
    }
}
