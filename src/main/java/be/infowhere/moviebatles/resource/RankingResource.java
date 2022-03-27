package be.infowhere.moviebatles.resource;

import be.infowhere.moviebatles.exceptions.RankingException;
import be.infowhere.moviebatles.service.RankingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Ranking Controller",tags = {"Ranking"})
@RestController
@RequestMapping("/ranking/v1")
public class RankingResource {

    private final RankingService rankingService;

    @Autowired
    public RankingResource(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity getNextQuestion() throws RankingException {
        return new ResponseEntity(
                rankingService.buildRanking(),
                HttpStatus.OK
        );
    }

}
