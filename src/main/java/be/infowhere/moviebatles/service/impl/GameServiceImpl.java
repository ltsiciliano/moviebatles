package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.User;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.GameException;
import be.infowhere.moviebatles.repository.GameRepository;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.MoviePlayService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import com.google.inject.internal.util.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final MoviePlayService moviePlayService;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository,
                           MoviePlayService moviePlayService
    ) {
        this.gameRepository = gameRepository;
        this.moviePlayService = moviePlayService;
    }

    @Transactional
    @Override
    public Game startNewGame(User user) throws GameException {
        if(gameRepository.findByUserAndStatus(user,StatusGameEnum.ONGOING).isPresent()){
            throw new GameException(String.format(MessagesUtils.errorGameOngoing,null));
        }

        Game gameOngoing = gameRepository.save(
                new Game(
                        user,
                        new HashSet<>(),
                        StatusGameEnum.ONGOING
                )
        );

        gameOngoing.setGamePlay(
                Stream.of(moviePlayService.nextQuestion(gameOngoing)).collect(Collectors.toSet())
        );

        return gameRepository.saveAndFlush(gameOngoing);
    }

    @Transactional
    @Override
    public void finishGame(User user) throws GameException {
        Optional<Game> gameOngoing = getGame(user);
        gameOngoing.get().setStatus(StatusGameEnum.CLOSED);
        gameRepository.saveAndFlush(gameOngoing.get());
    }

    @Override
    public Optional<Game> getGameByStatus(User user, StatusGameEnum status) {
        return gameRepository.findByUserAndStatus(user,status);
    }

    @Override
    public List<Game> findAllByStatus(StatusGameEnum status) {
        return gameRepository.findByStatus(status);
    }

    private Optional<Game> getGame(User user) throws GameException {
        Optional<Game> gameOngoing = gameRepository.findByUserAndStatus(user, StatusGameEnum.ONGOING);

        if (!gameOngoing.isPresent()) {
            throw new GameException(String.format(MessagesUtils.errorNoGameOngoing, null));
        }
        return gameOngoing;
    }

}
