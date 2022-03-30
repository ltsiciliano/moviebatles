package be.infowhere.moviebatles.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "MOVIE_PLAY")
public class MoviePlay {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    @ManyToOne
    private Movie firstMovie;

    @ManyToOne
    private Movie secondMovie;

    @ManyToOne
    private Movie answer;

    public MoviePlay(Movie firstMovie, Movie secondMovie) {
        this.firstMovie = firstMovie;
        this.secondMovie = secondMovie;
    }

    public MoviePlay() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getFirstMovie() {
        return firstMovie;
    }

    public void setFirstMovie(Movie firstMovie) {
        this.firstMovie = firstMovie;
    }

    public Movie getSecondMovie() {
        return secondMovie;
    }

    public void setSecondMovie(Movie secondMovie) {
        this.secondMovie = secondMovie;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Movie getAnswer() {
        return answer;
    }

    public void setAnswer(Movie answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoviePlay moviePlay = (MoviePlay) o;
        return Objects.equals(game, moviePlay.game) && Objects.equals(firstMovie, moviePlay.firstMovie) && Objects.equals(secondMovie, moviePlay.secondMovie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, firstMovie, secondMovie);
    }
}
