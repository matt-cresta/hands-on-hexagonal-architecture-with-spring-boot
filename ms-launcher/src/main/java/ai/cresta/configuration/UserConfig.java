package ai.cresta.configuration;

import ai.cresta.adapters.UserJpaAdapter;
import ai.cresta.ports.api.UserServicePort;
import ai.cresta.ports.spi.UserPersistencePort;
import ai.cresta.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserConfig {

    @Bean
    public UserPersistencePort userPersistence() {
        return new UserJpaAdapter();
    }

    @Bean
    public UserServicePort userService() {
        return new UserServiceImpl(userPersistence());
    }
}