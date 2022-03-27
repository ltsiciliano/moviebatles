package be.infowhere.moviebatles.service.impl;

import be.infowhere.moviebatles.domain.Game;
import be.infowhere.moviebatles.domain.MoviePlay;
import be.infowhere.moviebatles.dto.RankingDto;
import be.infowhere.moviebatles.enums.StatusGameEnum;
import be.infowhere.moviebatles.exceptions.RankingException;
import be.infowhere.moviebatles.service.GameService;
import be.infowhere.moviebatles.service.RankingService;
import be.infowhere.moviebatles.utils.MessagesUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RankingServiceImpl implements RankingService {

    private final GameService gameService;

    @Autowired
    public RankingServiceImpl(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public List<RankingDto> buildRanking() throws RankingException {

        List<Game> games =  gameService.findAllByStatus(StatusGameEnum.CLOSED);

        if(games==null || games.isEmpty()){
            throw new RankingException(MessagesUtils.errorNoDatasToRanking);
        }

        Map<String, List<Game>> listGamesByUser =
                games
                .stream()
                .collect(
                    Collectors.groupingBy(game -> game.getUser().getName())
                );

        return listGamesByUser
                .keySet()
                .stream()
                .map(user ->
                    new RankingDto(
                            user,
                            listGamesByUser.get(user).stream().mapToLong(game->game.getGamePlay().size()).sum(),
                            listGamesByUser.get(user).stream().map(game -> getQtdCorrectAnswers(game.getGamePlay())
                            ).reduce(0L,Long::sum)
                    )
                ).map(rankingDto ->{
                    rankingDto.setPercentAnswerCorrect(
                            rankingDto.getNumQuestionsAnswer() - rankingDto.getNumQtdCorrect()==0 ?
                                    100 * rankingDto.getNumQtdCorrect() :
                            (((rankingDto.getNumQtdCorrect() * 100)
                                    /rankingDto.getNumQuestionsAnswer())
                                    *rankingDto.getNumQtdCorrect())
                    );
                    return rankingDto;
                }
                )
                .sorted(Comparator.comparingDouble(RankingDto::getPercentAnswerCorrect).reversed())
                .collect(Collectors.toList());

    }

    private long getQtdCorrectAnswers(Set<MoviePlay> moviePlay) {
        return moviePlay.stream().filter(
                mp->
                        ((
                                NumberUtils.createDouble(mp.getSecondMovie().getImdbRating())*
                                        NumberUtils.createDouble(mp.getSecondMovie().getImdbVotes()))
                                >
                        (NumberUtils.createDouble(mp.getFirstMovie().getImdbRating())*
                                NumberUtils.createDouble(mp.getFirstMovie().getImdbVotes()))
                        &&
                                mp.getAnswer().getImdbID().equals(mp.getSecondMovie().getImdbID()))
                                ||
                        ((NumberUtils.createDouble(mp.getFirstMovie().getImdbRating())*
                                NumberUtils.createDouble(mp.getFirstMovie().getImdbVotes()))
                                >
                        (NumberUtils.createDouble(mp.getSecondMovie().getImdbRating())*
                                NumberUtils.createDouble(mp.getSecondMovie().getImdbVotes()))
                        &&
                                mp.getAnswer().getImdbID().equals(mp.getFirstMovie().getImdbID()))

        ).count();
    }

}
