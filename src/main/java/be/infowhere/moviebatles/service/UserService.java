package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService,UserDetails {
    Optional<User> findByLogin(String user);
    String findCurrentUserName();
}
