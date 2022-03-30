package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.dto.MoviePlayDto;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.mapper.MoviePlayMapper;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.MoviePlayService;
import be.infowhere.moviebatles.service.UserService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Movie Plat Controller",tags = {"Movie Play"})
@RestController
@RequestMapping("/movieplay/v1")
public class MoviePlayResource {

    private final MoviePlayMapper moviePlayMapper;
    private final MoviePlayService moviePlayService;
    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public MoviePlayResource(
            MoviePlayMapper moviePlayMapper,
            MoviePlayService moviePlayService,
            GameService gameService,
            UserService userService) {
        this.moviePlayMapper = moviePlayMapper;
        this.moviePlayService = moviePlayService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/next")
    public ResponseEntity getNextQuestion() throws GameException {
        Game gameOngoing = getGameOngoing(
                userService.findByLogin(userService.findCurrentUserName()).orElseThrow(()-> new GameException("Usuário não localizado"))
        );

        MoviePlay moviePlay = moviePlayService.nextQuestion(gameOngoing);

        moviePlayMapper.mapperMoviePlay(moviePlay);

        return new ResponseEntity(
                moviePlayMapper.mapperMoviePlay(moviePlay),
                HttpStatus.OK
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/answer",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postAnswerQuestion(
            @RequestBody MoviePlayDto moviePlayDto
    ) throws GameException {
        Game gameOngoing = getGameOngoing(
                userService.findByLogin(userService.findCurrentUserName()).orElseThrow(()-> new GameException("Usuário não localizado"))
        );

        MoviePlay moviePlay = moviePlayMapper.mapperMoviePlay(moviePlayDto);
        moviePlay.setGame(gameOngoing);

        MoviePlay moviePlayAnswer = moviePlayService.answerQuestion(
            moviePlay
        );

        return new ResponseEntity(
                moviePlayMapper.mapperMoviePlay(moviePlayAnswer),
                HttpStatus.OK
        );
    }

    private Game getGameOngoing(User user) throws GameException {
        return gameService.getGameByStatus(user, StatusGameEnum.ONGOING)
                .orElseThrow(() -> new GameException(MessagesUtils.errorNoGameOngoing));
    }



}
