package be.infowhere.moviebatles.dto;

import be.infowhere.moviebatles.enums.StatusGameEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDto {

    @JsonProperty("user")
    private UserDto user;

    @JsonProperty("gamePlay")
    private Set<MoviePlayDto> gamePlay;

    @JsonProperty("status")
    private StatusGameEnum status;

    public GameDto() {
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Set<MoviePlayDto> getGamePlay() {
        return gamePlay;
    }

    public void setGamePlay(Set<MoviePlayDto> gamePlay) {
        this.gamePlay = gamePlay;
    }

    public StatusGameEnum getStatus() {
        return status;
    }

    public void setStatus(StatusGameEnum status) {
        this.status = status;
    }

}
