package be.infowhere.moviebatles.support;

import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.dto.MovieDto;
import be.infowhere.moviebatles.dto.MoviePlayDto;

public class MoviePlayDtoExampleSupport {

    public static MoviePlayDto buildMoviePlayDto() {

        MovieDto movie1 = new MovieDto();
        movie1.setImdbID("tt9999");

        MovieDto movie2 = new MovieDto();
        movie2.setImdbID("tt8888");

        MoviePlayDto moviePlayDto = new MoviePlayDto();
        moviePlayDto.setId(1L);
        moviePlayDto.setFirstMovie(movie1);
        moviePlayDto.setSecondMovie(movie2);

        moviePlayDto.setAnswer(movie1);

        return moviePlayDto;
    }

}
