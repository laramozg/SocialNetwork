package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profile_game")
public class ProfileGame {
    @EmbeddedId
    private ProfileGameId id;

    @ManyToOne
    @MapsId("profileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profileId;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id", nullable = false)
    private Game gameId;

    public Profile getProfileId() {
        return profileId;
    }

    public void setProfileId(Profile profileId) {
        this.profileId = profileId;
    }

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    public ProfileGameId getId() {
        return id;
    }

    public void setId(ProfileGameId id) {
        this.id = id;
    }
}
