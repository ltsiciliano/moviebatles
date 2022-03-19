package be.infowhere.moviebatles.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoviePlayDto {

    @JsonProperty("game")
    private GameDto game;

    @JsonProperty("firstMovie")
    private MovieDto firstMovie;

    @JsonProperty("secondMovie")
    private MovieDto secondMovie;

    @JsonProperty("answer")
    private MovieDto answer;

    public GameDto getGame() {
        return game;
    }

    public void setGame(GameDto game) {
        this.game = game;
    }

    public MovieDto getFirstMovie() {
        return firstMovie;
    }

    public void setFirstMovie(MovieDto firstMovie) {
        this.firstMovie = firstMovie;
    }

    public MovieDto getSecondMovie() {
        return secondMovie;
    }

    public void setSecondMovie(MovieDto secondMovie) {
        this.secondMovie = secondMovie;
    }

    public MovieDto getAnswer() {
        return answer;
    }

    public void setAnswer(MovieDto answer) {
        this.answer = answer;
    }
}
