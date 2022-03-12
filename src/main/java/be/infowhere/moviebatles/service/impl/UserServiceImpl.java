package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.repository.UserRepository;
import be.infowhere.moviebatles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByLogin(String user) {
        return userRepository.findByLogin(user);
    }
}
