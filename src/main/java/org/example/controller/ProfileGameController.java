package org.example.controller;

import org.example.dto.GameDto;
import org.example.service.ProfileGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile-games")
public class ProfileGameController {

    private final ProfileGameService profileGameService;

    @Autowired
    public ProfileGameController(ProfileGameService profileGameService) {
        this.profileGameService = profileGameService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProfileGame(
            @RequestParam Integer profileId,
            @RequestParam Integer gameId) {
        profileGameService.addProfileToGame(profileId, gameId);
        return ResponseEntity.status(HttpStatus.CREATED).body("done");
    }

    @GetMapping("/profile/{profileId}/games")
    public ResponseEntity<List<GameDto>> getAllGamesByProfileId(@PathVariable Integer profileId) {
        List<GameDto> games = profileGameService.getAllGamesByProfileId(profileId);
        return ResponseEntity.ok(games);
    }
}
