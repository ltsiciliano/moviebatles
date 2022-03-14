package be.infowhere.moviebatles.domain;

import be.infowhere.moviebatles.enums.StatusGameEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "GAME")
public class Game {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "game")
    private Set<MoviePlay> gamePlay;

    @Column(nullable = false)
    private StatusGameEnum status;

    public Game() {
    }

    public Game(User user, Set<MoviePlay> gamePlay, StatusGameEnum status) {
        this.user = user;
        this.gamePlay = gamePlay;
        this.status = status;
    }

    public Game(Long id, User user, Set<MoviePlay> gamePlay, StatusGameEnum status) {
        this.id = id;
        this.user = user;
        this.gamePlay = gamePlay;
        this.status = status;
    }

    public Set<MoviePlay> getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(Set<MoviePlay> gamePlay) {
        this.gamePlay = gamePlay;
    }

    public StatusGameEnum getStatus() {
        return status;
    }

    public void setStatus(StatusGameEnum statusGame) {
        this.status = statusGame;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
