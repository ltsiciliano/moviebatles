package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.dto.GameDto;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.mapper.GameMapper;
import be.infowhere.moviebatles.service.GameService;
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

    @Autowired
    public GameResource(
            GameService gameService,
            GameMapper gameMapper
    ) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/new")
    public ResponseEntity getNewGame() throws GameException {

        User user = new User(1L,"leandro","lelele","lalala");

        GameDto gameDto = gameMapper.mapperGame(
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
        User user = new User(1L,"leandro","lelele","lalala");
        gameService.finishGame(user);
    }

}
