package org.example.repository;

import org.example.model.Profile;
import org.example.model.ProfileGame;
import org.example.model.ProfileGameId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileGameRepository extends JpaRepository<ProfileGame, ProfileGameId> {
    List<ProfileGame> findAllByProfileId(Profile profile);
}
