package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.GameDto;
import org.example.service.GameService;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/games/*")
public class GameServlet extends BaseServlet {

    private GameService gameService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.gameService = (GameService) config.getServletContext().getAttribute("gameService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // GET /games - get all games
                List<GameDto> games = gameService.getAllGames();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectMapper.writeValue(resp.getWriter(), games);
            } else {
                // GET /games/{id} - get game by id
                int gameId = extractIdFromPath(req);
                GameDto gameDto = gameService.getGameById(gameId);
                if (gameDto != null) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectMapper.writeValue(resp.getWriter(), gameDto);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BufferedReader reader = req.getReader();
            GameDto gameDto = objectMapper.readValue(reader, GameDto.class);
            int gameId = gameService.saveGame(gameDto);
            writeResponse(resp, HttpServletResponse.SC_CREATED, "Game created with ID: " + gameId);
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private int extractIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return Integer.parseInt(pathInfo.substring(1));
    }
}