package be.infowhere.moviebatles.service.user;

import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.repository.UserRepository;
import be.infowhere.moviebatles.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:/properties/omdbapi_tst.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUserByLogin() throws Exception{

        final String login = "joca";

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(
                new User(1L,"joaquim", login,"1234")
        ));

        Assertions.assertEquals(login,userService.findByLogin(login).get().getLogin());

    }

}
