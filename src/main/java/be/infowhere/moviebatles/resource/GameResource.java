package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.dto.GameDto;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.mapper.GameMapper;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.UserService;
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
@RequestMapping("/game/v1")
public class GameResource {

    private final GameService gameService;
    private final GameMapper gameMapper;
    private final UserService userService;

    @Autowired
    public GameResource(
            GameService gameService,
            GameMapper gameMapper,
            UserService userService) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/new")
    public ResponseEntity getNewGame() throws GameException {

        User user = userService.findByLogin(userService.findCurrentUserName()).orElseThrow(()-> new GameException("Usuário não localizado"));

        GameDto gameDto = gameMapper.mapperGameToDto(
                gameService.startNewGame(user)
        );

        return new ResponseEntity(
                gameDto,
                HttpStatus.OK
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/finish")
    public void getFinishGame() throws GameException {
        gameService.finishGame(
                userService.findByLogin(userService.findCurrentUserName()).orElseThrow(()-> new GameException("Usuário não localizado"))
        );
    }

}
