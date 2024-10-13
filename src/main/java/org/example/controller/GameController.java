package org.example.controller;

import org.example.dto.GameDto;
import org.example.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> findAllGames() {
        return ResponseEntity.ok(gameService.findAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDto> findGameById(@PathVariable Integer id) {
        GameDto game = gameService.findGameById(id);
        return ResponseEntity.ok(game);
    }

    @PostMapping
    public ResponseEntity<GameDto> createGame(@RequestBody GameDto game) {
        GameDto createdGame = gameService.createGame(game);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }
}