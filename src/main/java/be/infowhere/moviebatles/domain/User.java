package be.infowhere.moviebatles.domain;

import lombok.NonNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @OneToMany
    private List<Game> games;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idRole"))
    private List<Role> grantedRole;

    public User() {
    }

    public User(Long id, @NonNull String name, @NonNull String login, @NonNull String password) {
        this.idUser = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, String password, List<Role> grantedRole) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.grantedRole = grantedRole;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long id) {
        this.idUser = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Role> getGrantedAuthority() {
        return grantedRole;
    }

    public void setGrantedAuthority(List<Role> grantedRole) {
        this.grantedRole = grantedRole;
    }
}
