package ai.cresta.service;

import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.spi.UserPersistencePort;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.Configuration;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;

@AllArgsConstructor
public class GenesysWebChatServiceImpl implements MessageServicePort {

    private final SimpMessagingTemplate messageTemplate;
    private final UserPersistencePort userPersistencePort;

    @Override
    public void notifyFrontend(String message) {
        messageTemplate.convertAndSend("/topic/messages", message);
        
    }

    @Override
    public void notifyUser(String id, String message) {
        messageTemplate.convertAndSendToUser(id ,"/topic/private-messages", message);
    }

    
    @Override
    public void setAccessToken(String userId, String accessToken) {
        PureCloudRegionHosts region = PureCloudRegionHosts.us_west_2;
        ApiClient apiClient = ApiClient.Builder.standard().withBasePath(region).build();
        apiClient.setAccessToken(accessToken);
        Configuration.setDefaultApiClient(apiClient);
    }
}
