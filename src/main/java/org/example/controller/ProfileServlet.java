package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ProfileDto;
import org.example.service.ProfileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile/*")
public class ProfileServlet extends BaseServlet {

    private ProfileService profileService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProfileServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.profileService = (ProfileService) config.getServletContext().getAttribute("profileService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (isPathInfoInvalid(pathInfo, resp, "User ID is missing.")) return;

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));
            ProfileDto profileDto = profileService.getProfileByUserId(userId);
            if (profileDto != null) {
                String jsonResponse = objectMapper.writeValueAsString(profileDto);
                resp.setContentType("application/json");
                writeResponse(resp, HttpServletResponse.SC_OK, jsonResponse);
            } else {
                writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Profile not found");
            }
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving profile.");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BufferedReader reader = req.getReader();
            ProfileDto profileDto = objectMapper.readValue(reader, ProfileDto.class);
            int profileId = profileService.saveProfile(profileDto);
            writeResponse(resp, HttpServletResponse.SC_CREATED, "Profile created with ID: " + profileId);
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error created profile.");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            BufferedReader reader = req.getReader();
            ProfileDto profileDto = objectMapper.readValue(reader, ProfileDto.class);
            profileService.updateProfile(profileDto);
            writeResponse(resp, HttpServletResponse.SC_OK, "Profile updated successfully.");
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating profile.");
        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if (isPathInfoInvalid(pathInfo, resp, "User ID is missing.")) return;

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));
            profileService.deleteProfile(userId);
            writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "Profile deleted successfully.");
        } catch (SQLException | NumberFormatException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting profile.");
        }
    }
}