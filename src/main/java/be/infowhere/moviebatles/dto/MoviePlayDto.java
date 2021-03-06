package be.infowhere.moviebatles.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoviePlayDto {

    private Long id;

    @JsonProperty("firstMovie")
    private MovieDto firstMovie;

    @JsonProperty("secondMovie")
    private MovieDto secondMovie;

    @JsonProperty("answer")
    private MovieDto answer;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
