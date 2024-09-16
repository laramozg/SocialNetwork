package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.GameDto;
import org.example.service.ProfileGameService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/profile-game/*")
public class ProfileGameServlet extends BaseServlet {
    private ProfileGameService profileGameService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProfileGameServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.profileGameService = (ProfileGameService) config.getServletContext().getAttribute("profileGameService");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int profileId = Integer.parseInt(request.getParameter("profileId"));
            int gameId = Integer.parseInt(request.getParameter("gameId"));

            profileGameService.addProfileToGame(profileId, gameId);
            writeResponse(response, HttpServletResponse.SC_OK, "Adding profile to game.");
        } catch (SQLException e) {
            writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error adding profile to game.");
        } catch (NumberFormatException e) {
            writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid profile or game ID.");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (isPathInfoInvalid(pathInfo, response, "Profile ID is missing.")) return;

        try {
            int profileId = Integer.parseInt(pathInfo.substring(1));
            List<GameDto> games = profileGameService.getGamesByProfileId(profileId);

            String jsonResponse = objectMapper.writeValueAsString(games);
            writeResponse(response, HttpServletResponse.SC_OK, jsonResponse);
        } catch (SQLException | IOException e) {
            writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving games for profile.");
        } catch (NumberFormatException e) {
            writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid profile ID.");
        }
    }
}

