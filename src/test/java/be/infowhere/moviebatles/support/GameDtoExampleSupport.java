package be.infowhere.moviebatles.support;

import be.infowhere.moviebatles.dto.GameDto;
import be.infowhere.moviebatles.dto.MovieDto;
import be.infowhere.moviebatles.dto.MoviePlayDto;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import com.google.common.collect.Sets;

public class GameDtoExampleSupport {

    public static GameDto buildGame(){
        GameDto gameDto = new GameDto();

        MoviePlayDto moviePlayDto = new MoviePlayDto();

        MovieDto movie1 = new MovieDto();
        movie1.setTitle("test1");
        movie1.setImdbID("tt12345");

        moviePlayDto.setFirstMovie(movie1);

        MovieDto movie2 = new MovieDto();
        movie2.setTitle("test1");
        movie2.setImdbID("tt12345");

        moviePlayDto.setSecondMovie(movie2);

        gameDto.setGamePlay(Sets.newHashSet(moviePlayDto));
        gameDto.setStatus(StatusGameEnum.ONGOING);

        return gameDto;

    }

}
