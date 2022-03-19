package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.repository.GameRepository;
import be.infowhere.moviebatles.repository.MoviePlayRepository;
import be.infowhere.moviebatles.repository.MovieRepository;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import be.infowhere.moviebatles.utils.NumberUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Set;

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
    private MovieRepository movieRepository;

    @MockBean
    private NumberUtils numberUtils;

    @MockBean
    MoviePlayRepository moviePlayRepository;

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
        when(movieRepository.findByImdbID(any())).thenReturn(
                Optional.of(new Movie())
        );

        when(numberUtils.getQtdCombinations(any())).thenReturn(1);
        when(numberUtils.getRandomNumber(anyInt())).thenReturn(1).thenReturn(2);

        try{
            gameService.startNewGame(user);
        }catch (GameException ge){
            fail();
        }

        verify(movieRepository,times(2)).findByImdbID(any());
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

    //NEXT QUESTION
    @Test
    public void nextQuestionGameWhenThereIsNoGameOngoing_ERROR() throws Exception{

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.empty()
        );

        try{
            gameService.nextQuestion(user);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertEquals(String.format(MessagesUtils.errorNoGameOngoing,user.getGames()),ge.getMessage());
        }
    }

    @Test
    public void nextQuestionGameWhenThereIsOpenQuestion_ERROR() throws Exception{

        MoviePlay moviePlay = buildMoviePlay();
        Game game = new Game(1L,user, Set.of(moviePlay),StatusGameEnum.ONGOING);

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.of(game)
        );

        try{
            gameService.nextQuestion(user);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertEquals(MessagesUtils.errorThereIsOpenQuestionToAnswer,ge.getMessage());
        }
    }

    @Test
    public void nextQuestionGameWhenThereIsMaxCombinationReached_ERROR() throws Exception{

        MoviePlay moviePlay = buildMoviePlay();
        moviePlay.setAnswer(new Movie());

        Game game = new Game(1L,user, Set.of(moviePlay),StatusGameEnum.ONGOING);

        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(
                Optional.of(game)
        );

        when(numberUtils.getQtdCombinations(any())).thenReturn(1);

        try{
            gameService.nextQuestion(user);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertEquals(MessagesUtils.errorMaxNumberCombinationQuestionReached,ge.getMessage());
        }
    }


    @Test
    public void nextQuestionGameWithQuestion() throws Exception{

        MoviePlay moviePlay1 = buildMoviePlay();
        moviePlay1.getFirstMovie().setImdbID("tt11111");
        moviePlay1.getSecondMovie().setImdbID("tt2222");
        moviePlay1.setAnswer(moviePlay1.getFirstMovie());

        MoviePlay moviePlay2 = buildMoviePlay();
        moviePlay1.getFirstMovie().setImdbID("tt11111");
        moviePlay1.getSecondMovie().setImdbID("tt99999");
        moviePlay2.setAnswer(moviePlay1.getFirstMovie());

        Game game = new Game(1L,user, Set.of(moviePlay1,moviePlay2),StatusGameEnum.ONGOING);
        Movie firstMovie = buildMovie("tt2222",10L);
        Movie secondMovie = buildMovie("tt99999",11L);

        when(numberUtils.getRandomNumber(anyInt())).thenReturn(1).thenReturn(2);
        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(Optional.of(game));
        when(movieRepository.findByImdbID("tt2222")).thenReturn(Optional.of(firstMovie));
        when(movieRepository.findByImdbID("tt99999")).thenReturn(Optional.of(secondMovie));

        ArgumentCaptor<MoviePlay> moviePlayArgumentCaptor = ArgumentCaptor.forClass(MoviePlay.class);
        when(moviePlayRepository.save(moviePlayArgumentCaptor.capture())).thenReturn(new MoviePlay());

        try{
            gameService.nextQuestion(user);
            assertEquals(
                    ("tt2222" + "tt99999").hashCode(),
                    (moviePlayArgumentCaptor.getValue().getFirstMovie().getImdbID()+moviePlayArgumentCaptor.getValue().getSecondMovie().getImdbID()).hashCode());
        }catch (GameException ge){
            fail();
        }
    }

    private Movie buildMovie(String imdbID,Long id) {

        Movie movie = new Movie();
        movie.setImdbID(imdbID);
        movie.setId(id);
        return movie;

    }

    private MoviePlay buildMoviePlay() {

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
