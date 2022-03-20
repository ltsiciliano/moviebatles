package be.infowhere.moviebatles.support;

import be.infowhere.moviebatles.domain.Movie;

public class MovieExampleSupport {

    public static Movie buildMovie(String imdbID, Long id) {

        Movie movie = new Movie();
        movie.setImdbID(imdbID);
        movie.setId(id);
        return movie;

    }

}
