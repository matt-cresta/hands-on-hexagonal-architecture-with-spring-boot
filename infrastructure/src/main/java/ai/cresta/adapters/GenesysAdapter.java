package ai.cresta.adapters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
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

public class GenesysAdapter implements GenesysPort {

    NotificationsApi notificationsApi = new NotificationsApi();

    

    @Override
    public void createNotificationChannel() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler sessionHandler = new genesysSessionHandlerAdapter();
       
       try{

        Channel channel = notificationsApi.postNotificationsChannels();
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
        
    }

    @Override
    public void subscribeToConversation(String conversationId, Channel channel) {
       String channelId = channel.getId();
       ChannelTopic channelTopic = new ChannelTopic().id(channelId);

       List<ChannelTopic> body = Arrays.asList(channelTopic); 

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
