package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.dto.RankingDto;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.RankingException;
import be.infowhere.moviebatles.repository.GameRepository;
import be.infowhere.moviebatles.support.GameExampleSupport;
import be.infowhere.moviebatles.support.MoviePlayExampleSupport;
import be.infowhere.moviebatles.utils.MessagesUtils;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:/properties/omdbapi_tst.properties")
public class RankingServiceTest {

    private static final String USER_1 = "user1";
    private static final String USER_1_LOGIN = "user1login";
    private static final String USER_2 = "user2";
    private static final String USER_2_LOGIN = "user2login";

    @Autowired
    private RankingService rankingService;

    @MockBean
    private GameRepository gameRepository;

    @Test
    public void rankingExceptionWithoutDatas_ERROR() throws Exception{

        when(gameRepository.findByStatus(any())).thenReturn(
                new ArrayList<>()
        );

        try{
            rankingService.buildRanking();
            fail();
        }catch (RankingException re){
            assertNotNull(re);
            assertEquals(MessagesUtils.errorNoDatasToRanking,re.getMessage());
        }
    }

    @Test
    public void rankingExceptionWithDatas_OK() throws Exception{

        when(gameRepository.findByStatus(any())).thenReturn(
                buildGames().stream()
                        .collect(Collectors.toList())
        );

        //User 1 - Game 1 => 2 pts
        //User 1 - Game 2 => 0 pts
        //User 2 - Game 1 => 3 pts

        //User 1 - Answer: 6 - Rights: 3 - Errors: 3
        // (3*100)/6 * 3 => 150
        //User 2 - Answer: 3 - Rights: 2 - Erros: 1
        // (2*100)/3 * 2 = 133,33

        try{
            List<RankingDto> ranking = rankingService.buildRanking();
            assertEquals(USER_1,ranking.get(0).getUser());
            assertEquals(USER_2,ranking.get(1).getUser());
            assertEquals(150.0,ranking.get(0).getPercentAnswerCorrect());
            assertEquals(
                    NumberUtils.createBigDecimal("133.33").setScale(2, RoundingMode.HALF_UP),
                    NumberUtils.createBigDecimal(String.valueOf(ranking.get(1).getPercentAnswerCorrect())).setScale(2, RoundingMode.HALF_UP)
            );

        }catch (RankingException re){
            fail();
        }
    }

    private List<Game> buildGames(){
        List<Game> games = new ArrayList<>();

        //INSERT GAMES
        games.addAll(
                Sets.newHashSet(
                        buildGameFirstGame(),
                        buildGameSecondGame(),
                        buildGameThirdGame()
                )
        );

        return games;
    }

    private Game buildGameFirstGame(){

        //******* GAME 1 *******

        Game game1 = GameExampleSupport.buildGame();

        game1.setId(1L);
        game1.setStatus(StatusGameEnum.CLOSED);
        game1.setUser(new User(1L,USER_1, USER_1_LOGIN,"zzz"));

        //**** PLAY 1 ****
        MoviePlay moviePlay1 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay1.setId(1L);
        moviePlay1.getFirstMovie().setImdbID("tt0001");
        moviePlay1.getFirstMovie().setImdbRating("100.20");
        moviePlay1.getFirstMovie().setImdbVotes("10");
        //100.20 * 10 = 1.002.00

        moviePlay1.getSecondMovie().setImdbID("tt0002");
        moviePlay1.getSecondMovie().setImdbRating("90.32");
        moviePlay1.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay1.setAnswer(moviePlay1.getSecondMovie()); //1 ponto
        moviePlay1.setGame(game1);

        //**** PLAY 2 ****
        MoviePlay moviePlay2 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay2.setId(2L);
        moviePlay2.getFirstMovie().setImdbID("tt0003");
        moviePlay2.getFirstMovie().setImdbRating("602.1");
        moviePlay2.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay2.getSecondMovie().setImdbID("tt0002");
        moviePlay2.getSecondMovie().setImdbRating("90.32");
        moviePlay2.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay2.setAnswer(moviePlay2.getFirstMovie()); //0 ponto
        moviePlay2.setGame(game1);

        //**** PLAY 3 ****
        MoviePlay moviePlay3 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay3.setId(2L);
        moviePlay3.getFirstMovie().setImdbID("tt0003");
        moviePlay3.getFirstMovie().setImdbRating("602.1");
        moviePlay3.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay3.getSecondMovie().setImdbID("tt0004");
        moviePlay3.getSecondMovie().setImdbRating("3.32");
        moviePlay3.getSecondMovie().setImdbVotes("453");
        //3.32 * 453 = 1.503.96

        moviePlay3.setAnswer(moviePlay3.getFirstMovie()); //1 ponto
        moviePlay3.setGame(game1);

        game1.setGamePlay(Sets.newHashSet(
                moviePlay1,
                moviePlay2,
                moviePlay3
        ));

        return game1;
    }

    private Game buildGameSecondGame(){

        //******* GAME 2 *******

        Game game2 = GameExampleSupport.buildGame();

        game2.setId(2L);
        game2.setStatus(StatusGameEnum.CLOSED);
        game2.setUser(new User(1L,USER_1, USER_1_LOGIN,"zzz"));

        //**** PLAY 1 ****
        MoviePlay moviePlay1 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay1.setId(1L);
        moviePlay1.getFirstMovie().setImdbID("tt0001");
        moviePlay1.getFirstMovie().setImdbRating("100.20");
        moviePlay1.getFirstMovie().setImdbVotes("10");
        moviePlay1.setGame(game2);
        //100.20 * 10 = 1.002.00

        moviePlay1.getSecondMovie().setImdbID("tt0002");
        moviePlay1.getSecondMovie().setImdbRating("90.32");
        moviePlay1.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay1.setAnswer(moviePlay1.getSecondMovie()); //1 ponto
        moviePlay1.setGame(game2);

        //**** PLAY 2 ****
        MoviePlay moviePlay2 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay2.setId(2L);
        moviePlay2.getFirstMovie().setImdbID("tt0003");
        moviePlay2.getFirstMovie().setImdbRating("602.1");
        moviePlay2.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay2.getSecondMovie().setImdbID("tt0002");
        moviePlay2.getSecondMovie().setImdbRating("90.32");
        moviePlay2.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay2.setAnswer(moviePlay2.getFirstMovie()); //0 ponto
        moviePlay2.setGame(game2);

        //**** PLAY 3 ****
        MoviePlay moviePlay3 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay3.setId(2L);
        moviePlay3.getFirstMovie().setImdbID("tt0003");
        moviePlay3.getFirstMovie().setImdbRating("602.1");
        moviePlay3.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay3.getSecondMovie().setImdbID("tt0004");
        moviePlay3.getSecondMovie().setImdbRating("3.32");
        moviePlay3.getSecondMovie().setImdbVotes("453");
        //3.32 * 453 = 1.503.96

        moviePlay3.setAnswer(moviePlay3.getSecondMovie()); //0 ponto
        moviePlay3.setGame(game2);

        game2.setGamePlay(Sets.newHashSet(
                moviePlay1,
                moviePlay2,
                moviePlay3
        ));

        return game2;
    }

    private Game buildGameThirdGame(){

        //******* GAME 3 *******

        Game game3 = GameExampleSupport.buildGame();

        game3.setId(2L);
        game3.setStatus(StatusGameEnum.CLOSED);
        game3.setUser(new User(1L,USER_2, USER_2_LOGIN,"zzz"));

        //**** PLAY 1 ****
        MoviePlay moviePlay1 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay1.setId(1L);
        moviePlay1.getFirstMovie().setImdbID("tt0001");
        moviePlay1.getFirstMovie().setImdbRating("100.20");
        moviePlay1.getFirstMovie().setImdbVotes("10");
        //100.20 * 10 = 1.002.00

        moviePlay1.getSecondMovie().setImdbID("tt0002");
        moviePlay1.getSecondMovie().setImdbRating("90.32");
        moviePlay1.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay1.setAnswer(moviePlay1.getFirstMovie()); //0 ponto
        moviePlay1.setGame(game3);

        //**** PLAY 2 ****
        MoviePlay moviePlay2 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay2.setId(2L);
        moviePlay2.getFirstMovie().setImdbID("tt0003");
        moviePlay2.getFirstMovie().setImdbRating("602.1");
        moviePlay2.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay2.getSecondMovie().setImdbID("tt0002");
        moviePlay2.getSecondMovie().setImdbRating("90.32");
        moviePlay2.getSecondMovie().setImdbVotes("30");
        //90.32 * 30 = 2.709.60

        moviePlay2.setAnswer(moviePlay2.getSecondMovie()); //1 ponto
        moviePlay2.setGame(game3);

        //**** PLAY 3 ****
        MoviePlay moviePlay3 = MoviePlayExampleSupport.buildMoviePlay();
        moviePlay3.setId(2L);
        moviePlay3.getFirstMovie().setImdbID("tt0003");
        moviePlay3.getFirstMovie().setImdbRating("602.1");
        moviePlay3.getFirstMovie().setImdbVotes("3");
        //602.1 * 3 = 1.806.03

        moviePlay3.getSecondMovie().setImdbID("tt0004");
        moviePlay3.getSecondMovie().setImdbRating("3.32");
        moviePlay3.getSecondMovie().setImdbVotes("453");
        //3.32 * 453 = 1.503.96

        moviePlay3.setAnswer(moviePlay3.getFirstMovie()); //1 ponto
        moviePlay3.setGame(game3);

        game3.setGamePlay(Sets.newHashSet(
                moviePlay1,
                moviePlay2,
                moviePlay3
        ));

        return game3;
    }

}
