package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.dto.GameDto;
import be.infowhere.moviebatles.mapper.GameMapper;
import be.infowhere.moviebatles.mapper.GameMapperImpl;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.support.GameDtoExampleSupport;
import be.infowhere.moviebatles.support.GameExampleSupport;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameResource.class)
@Import(GameMapperImpl.class)
public class GameResourceTest {

    public static final String GAME_BASE_PATH = "/game/v1";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;
    @MockBean
    private GameMapper gameMapper;

    @Test
    public void getInvoiceById() throws Exception{

        when(gameService.startNewGame(any())).thenReturn(
                GameExampleSupport.buildGame()
        );
        GameDto gameDto = GameDtoExampleSupport.buildGame();
        when(gameMapper.mapperGame((Game)any())).thenReturn(
                gameDto
        );

        this.mockMvc.perform(
                get(GAME_BASE_PATH + "/new"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gamePlay[0].firstMovie.imdbID", Matchers.equalTo("tt12345")))
                .andExpect(jsonPath("$.gamePlay[0].answer", Matchers.nullValue()));
    }

}
