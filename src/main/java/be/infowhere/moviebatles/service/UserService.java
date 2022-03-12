package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String user);
}
