package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.dto.MoviePlayDto;
import be.infowhere.moviebatles.mapper.MoviePlayMapper;
import be.infowhere.moviebatles.mapper.MoviePlayMapperImpl;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.MoviePlayService;
import be.infowhere.moviebatles.support.GameExampleSupport;
import be.infowhere.moviebatles.support.MoviePlayDtoExampleSupport;
import be.infowhere.moviebatles.support.MoviePlayExampleSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoviePlayResource.class)
@Import(MoviePlayMapperImpl.class)
public class MoviePlayResourceTest {

    public static final String GAME_BASE_PATH = "/movieplay/v1";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MoviePlayService moviePlayService;
    @MockBean
    private MoviePlayMapper moviePlayMapper;
    @MockBean
    private GameService gameService;

    @Test
    public void answerQuestion() throws Exception{

        when(moviePlayService.answerQuestion(any())).thenReturn(
                MoviePlayExampleSupport.buildMoviePlay()
        );

        when(moviePlayMapper.mapperMoviePlay((MoviePlay) any())).thenReturn(
                MoviePlayDtoExampleSupport.buildMoviePlayDto()
        );
        when(moviePlayMapper.mapperMoviePlay((MoviePlayDto) any())).thenReturn(
            MoviePlayExampleSupport.buildMoviePlay()
        );

        when(gameService.getGameByStatus(any(),any())).thenReturn(
                Optional.of(GameExampleSupport.buildGame())
        );

        this.mockMvc.perform(
                post(GAME_BASE_PATH + "/answer")
                        .content(asJsonString(MoviePlayDtoExampleSupport.buildMoviePlayDto()))
                        .contentType("application/json;charset=UTF-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstMovie.imdbID", Matchers.equalTo("tt9999")));
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
