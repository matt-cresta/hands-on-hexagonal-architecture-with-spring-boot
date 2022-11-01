package ai.cresta.configuration;

import ai.cresta.adapters.BookJpaAdapter;
import ai.cresta.ports.api.BookServicePort;
import ai.cresta.ports.spi.BookPersistencePort;
import ai.cresta.service.BookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookConfig {

    @Bean
    public BookPersistencePort bookPersistence() {
        return new BookJpaAdapter();
    }

    @Bean
    public BookServicePort bookService() {
        return new BookServiceImpl(bookPersistence());
    }
}
