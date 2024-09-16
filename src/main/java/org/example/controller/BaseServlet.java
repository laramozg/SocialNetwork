package org.example.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected boolean isPathInfoInvalid(String pathInfo, HttpServletResponse resp, String message) {
        return writeResponseIfCondition(resp, pathInfo == null || pathInfo.equals("/"),
                HttpServletResponse.SC_BAD_REQUEST,
                message);
    }

    protected void writeResponse(HttpServletResponse resp, int status, String message) {
        try {
            resp.setStatus(status);
            resp.getWriter().write(message);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean writeResponseIfCondition(HttpServletResponse resp, boolean condition, int status, String message) {
        if (condition) {
            writeResponse(resp, status, message);
            return true;
        }
        return false;
    }
}