package be.infowhere.moviebatles.service;

import be.infowhere.moviebatles.dto.RankingDto;
import be.infowhere.moviebatles.exceptions.RankingException;

import java.util.List;

public interface RankingService {
    List<RankingDto> buildRanking() throws RankingException;
}
