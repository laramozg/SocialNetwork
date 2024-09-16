package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProfileDto;
import org.example.service.ProfileService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/profile/*")
public class ProfileServlet extends BaseServlet {

    private final ProfileService profileService;
    private final ObjectMapper objectMapper;

    public ProfileServlet(ProfileService profileService) {
        this.profileService = profileService;
        this.objectMapper = new ObjectMapper();
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
    public void doPut(HttpServletRequest req, HttpServletResponse resp){
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
    public void doDelete(HttpServletRequest req, HttpServletResponse resp){
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