package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.repository.UserRepository;
import be.infowhere.moviebatles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private String userName;
    private String password;
    private List<GrantedAuthority> grantedAuthority = new ArrayList<>();

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByLogin(String user) {
        return userRepository.findByLogin(user);
    }

    @Override
    public String findCurrentUserName() {
        return((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthority;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<GrantedAuthority> getGrantedAuthority() {
        return grantedAuthority;
    }

    @Override
    public UserDetails loadUserByUsername(String u) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(u).orElseThrow(
                ()-> new UsernameNotFoundException("Usuário não encontrado")
        );

        this.userName = user.getLogin();
        this.password = new BCryptPasswordEncoder().encode(user.getPassword());
        user.getGrantedAuthority().forEach(role -> {
            this.grantedAuthority.add((GrantedAuthority) () -> role.getAuthority());
        });

        return this;
    }
}
