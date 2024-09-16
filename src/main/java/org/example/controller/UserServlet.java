package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.UserDto;
import org.example.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/users/*")
public class UserServlet extends BaseServlet {
    private UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public UserServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.userService = (UserService) config.getServletContext().getAttribute("userService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (isPathInfoInvalid(pathInfo, resp, "User ID is missing.")) return;

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));
            UserDto userDto = userService.getUserById(userId);
            if (userDto != null) {
                String jsonResponse = objectMapper.writeValueAsString(userDto);
                resp.setContentType("application/json");
                writeResponse(resp, HttpServletResponse.SC_OK, jsonResponse);
            } else {
                writeResponse(resp, HttpServletResponse.SC_NOT_FOUND, "User not found.");
            }
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving user.");
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserDto userDto = objectMapper.readValue(req.getReader(), UserDto.class);
            int userId = userService.saveUser(userDto);
            writeResponse(resp, HttpServletResponse.SC_CREATED, "User created with ID: " + userId);
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating user.");
        }
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UserDto userDto = objectMapper.readValue(req.getReader(), UserDto.class);
            userService.updateUser(userDto);
            writeResponse(resp, HttpServletResponse.SC_OK, "User updated.");
        } catch (SQLException | IOException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating user.");

        }
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if (isPathInfoInvalid(pathInfo, resp, "User ID is missing.")) return;

        try {
            int userId = Integer.parseInt(pathInfo.substring(1));
            userService.deleteUser(userId);
            writeResponse(resp, HttpServletResponse.SC_NO_CONTENT, "User deleted.");
        } catch (SQLException | NumberFormatException e) {
            writeResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error deleting user.");
        }
    }

}