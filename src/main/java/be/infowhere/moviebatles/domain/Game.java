package be.infowhere.moviebatles.domain;

import javax.persistence.*;
import java.util.List;

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
    private List<MoviePlay> gamePlay;

    public void setGamePlay(List<MoviePlay> gamePlay) {
        this.gamePlay = gamePlay;
    }

    public List<MoviePlay> getGamePlay() {
        return gamePlay;
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
