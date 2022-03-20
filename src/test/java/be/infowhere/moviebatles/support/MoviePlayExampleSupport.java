package be.infowhere.moviebatles.support;

import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;

public class MoviePlayExampleSupport {

    public static MoviePlay buildMoviePlay() {

        Movie movie1 = new Movie();
        movie1.setId(1L);

        Movie movie2 = new Movie();
        movie2.setId(2L);

        MoviePlay moviePlay = new MoviePlay();
        moviePlay.setId(1L);
        moviePlay.setFirstMovie(movie1);
        moviePlay.setSecondMovie(movie2);

        moviePlay.setAnswer(null);

        return moviePlay;
    }

}
