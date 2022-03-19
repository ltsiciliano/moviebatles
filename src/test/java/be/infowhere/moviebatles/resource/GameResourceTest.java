package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.Movie;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.mapper.GameMapperImpl;
import be.infowhere.moviebatles.resource.GameResource;
import be.infowhere.moviebatles.service.GameService;
import com.google.common.collect.Sets;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameResource.class)
@Import(GameMapperImpl.class)
public class GameResourceTest {

    public static final String GAME_BASE_PATH = "/game/v1";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;

    @Test
    public void getInvoiceById() throws Exception{

        when(gameService.startNewGame(any())).thenReturn(
                buildGame()
        );

        this.mockMvc.perform(
                get(GAME_BASE_PATH + "/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gamePlay[0].firstMovie.imdbID", Matchers.equalTo("tt12345")))
                .andExpect(jsonPath("$.gamePlay[0].answer", Matchers.nullValue()));
    }

    private Game buildGame(){
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
