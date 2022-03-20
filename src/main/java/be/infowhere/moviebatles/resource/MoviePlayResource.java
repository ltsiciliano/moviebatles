package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.mapper.MoviePlayMapper;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.MoviePlayService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Game Controller",tags = {"Game"})
@RestController
@RequestMapping("/movieplay/v1")
public class MoviePlayResource {

    private final MoviePlayMapper moviePlayMapper;
    private final MoviePlayService moviePlayService;
    private final GameService gameService;

    @Autowired
    public MoviePlayResource(
            MoviePlayMapper moviePlayMapper,
            MoviePlayService moviePlayService,
            GameService gameService
    ) {
        this.moviePlayMapper = moviePlayMapper;
        this.moviePlayService = moviePlayService;
        this.gameService = gameService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/next")
    public ResponseEntity getNextQuestion() throws GameException {
        User user = new User(1L,"leandro","lelele","lalala");

        Game gameOngoing =
                gameService.getGameByStatus(user, StatusGameEnum.ONGOING)
                .orElseThrow(()->new GameException(MessagesUtils.errorNoGameOngoing));

        MoviePlay moviePlay = moviePlayService.nextQuestion(gameOngoing);

        moviePlayMapper.mapperMoviePlay(moviePlay);

        return new ResponseEntity(
                moviePlayMapper.mapperMoviePlay(moviePlay),
                HttpStatus.OK
        );
    }



}
