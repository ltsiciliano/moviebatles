package be.infowhere.moviebatles.config;

import be.infowhere.moviebatles.domain.Role;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.repository.RoleRepository;
import be.infowhere.moviebatles.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class CommandLineBeanConfig {

    @Bean
    public CommandLineRunner insertUsers(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            Role rolePersisted = roleRepository.save(new Role("GAMER"));
            Role role = roleRepository.findById(rolePersisted.getIdRole()).orElseThrow(()-> new RuntimeException("Erro ao carregar os dados"));
            userRepository.save(new User("sassa","mutema","1234", new ArrayList(){{add(role);}}));
            userRepository.save(new User("roque","santeiro","5678",new ArrayList(){{add(role);}}));
            userRepository.save(new User("carminha","malvada","9999",new ArrayList(){{add(role);}}));
        };
    }

}
