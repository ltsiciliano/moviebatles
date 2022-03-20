package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.repository.GameRepository;
import be.infowhere.moviebatles.support.MoviePlayExampleSupport;
import be.infowhere.moviebatles.utils.MessagesUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:/properties/omdbapi_tst.properties")
public class GameServiceTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private GameRepository gameRepository;

    @MockBean
    private MoviePlayService moviePlayService;

    private User user;

    @BeforeAll
    public void init(){
        user = new User(1L,"test","test","test");
    }

    @Test
    public void startNewGameWithGameToUserOngoing_ERROR() throws Exception{

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.of(new Game(1L,user,null,null))
        );

        try{
            gameService.startNewGame(user);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertEquals(String.format(MessagesUtils.errorGameOngoing,user.getGames()),ge.getMessage());
        }
    }

    @Test
    public void startNewGameWithoutGameToUserOngoing_OK() throws Exception{

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.empty()
        );

        when(moviePlayService.nextQuestion(any())).thenReturn(
                MoviePlayExampleSupport.buildMoviePlay()
        );


        try{
            gameService.startNewGame(user);
        }catch (GameException ge){
            fail();
        }

        verify(moviePlayService,times(1)).nextQuestion(any());
        verify(gameRepository,times(1)).save(any());
    }

    @Test
    public void finishOngoingGameWithoutGameOngoing_ERROR() throws Exception{

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.empty()
        );

        try{
            gameService.finishGame(user);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertEquals(String.format(MessagesUtils.errorNoGameOngoing,user.getGames()),ge.getMessage());
        }
    }

    @Test
    public void finishGameWhenGameOngoing() throws Exception{

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.of(new Game(1L,user,null,null))
        );

        try{
            gameService.finishGame(user);
        }catch (GameException ge){
            fail();
        }

        ArgumentCaptor<Game> gameCaptor = ArgumentCaptor.forClass(Game.class);

        verify(gameRepository,times(1)).saveAndFlush(gameCaptor.capture());
        assertEquals(StatusGameEnum.CLOSED,gameCaptor.getValue().getStatus());
    }

}
