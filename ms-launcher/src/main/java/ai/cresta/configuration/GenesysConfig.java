package ai.cresta.configuration;

import ai.cresta.adapters.GenesysAdapter;
import ai.cresta.ports.api.MessageServicePort;
import ai.cresta.ports.spi.GenesysPort;
import ai.cresta.service.GenesysWebChatServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenesysConfig {

    @Bean
    public GenesysPort genesysPort() { return new GenesysAdapter(); }

    @Bean
    public MessageServicePort messageServicePort(){ return new GenesysWebChatServiceImpl(genesysPort());}
}

