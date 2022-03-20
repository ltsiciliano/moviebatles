package be.infowhere.moviebatles.config;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandLineBeanConfig {

    @Bean
    public CommandLineRunner insertUsers(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("sassa","mutema","1234"));
            userRepository.save(new User("roque","santeiro","5678"));
            userRepository.save(new User("carminha","malvada","9999"));
        };
    }

}
