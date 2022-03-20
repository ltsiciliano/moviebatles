package be.infowhere.moviebatles.support;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import com.google.common.collect.Sets;

public class GameExampleSupport {

    public static Game buildGame(){
        Game game = new Game();

        MoviePlay moviePlay = new MoviePlay();

        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("test1");
        movie1.setImdbID("tt12345");

        moviePlay.setFirstMovie(movie1);

        Movie movie2 = new Movie();
        movie2.setId(1L);
        movie2.setTitle("test1");
        movie2.setImdbID("tt12345");

        moviePlay.setSecondMovie(movie2);

        game.setGamePlay(Sets.newHashSet(moviePlay));
        game.setStatus(StatusGameEnum.ONGOING);

        return game;

    }

}
