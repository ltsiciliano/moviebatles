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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static be.infowhere.moviebatles.support.MovieExampleSupport.buildMovie;
import static be.infowhere.moviebatles.support.MoviePlayExampleSupport.buildMoviePlay;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:/properties/omdbapi_tst.properties")
public class MoviePlayServiceTest {

    @Autowired
    private MoviePlayService moviePlayService;

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
    public void nextQuestionGameWhenThereIsNoGameOngoing_ERROR() throws Exception{

        try{
            moviePlayService.nextQuestion(null);
            fail();
        }catch (GameException ge){
            assertNotNull(ge);
            assertTrue(MessagesUtils.errorNoGameOngoing.contains(ge.getMessage()));
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
            moviePlayService.nextQuestion(game);
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
            moviePlayService.nextQuestion(game);
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

        Set<MoviePlay> moviePlays = new HashSet<>();
        moviePlays.add(moviePlay1);
        moviePlays.add(moviePlay2);

        Game game = new Game(1L,user, moviePlays,StatusGameEnum.ONGOING);
        Movie firstMovie = buildMovie("tt2222",10L);
        Movie secondMovie = buildMovie("tt99999",11L);

        when(numberUtils.getRandomNumber(anyInt())).thenReturn(1).thenReturn(2);
        when(gameRepository.findByUserAndStatus(any(),any())).thenReturn(Optional.of(game));
        when(movieRepository.findByImdbID("tt2222")).thenReturn(Optional.of(firstMovie));
        when(movieRepository.findByImdbID("tt99999")).thenReturn(Optional.of(secondMovie));

        ArgumentCaptor<MoviePlay> moviePlayArgumentCaptor = ArgumentCaptor.forClass(MoviePlay.class);
        when(moviePlayRepository.save(moviePlayArgumentCaptor.capture())).thenReturn(new MoviePlay());

        try{
            moviePlayService.nextQuestion(game);
            assertEquals(
                    ("tt2222" + "tt99999").hashCode(),
                    (moviePlayArgumentCaptor.getValue().getFirstMovie().getImdbID()+moviePlayArgumentCaptor.getValue().getSecondMovie().getImdbID()).hashCode());
        }catch (GameException ge){
            fail();
        }
    }



}
