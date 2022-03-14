package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByLogin(String user);
}
