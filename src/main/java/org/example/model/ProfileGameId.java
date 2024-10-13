package org.example.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfileGameId implements Serializable {
    private Integer profileId;
    private Integer gameId;

    public ProfileGameId() {}

    public ProfileGameId(Integer profileId, Integer gameId) {
        this.profileId = profileId;
        this.gameId = gameId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileGameId)) return false;
        ProfileGameId that = (ProfileGameId) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(gameId, that.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId, gameId);
    }
}