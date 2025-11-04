package com.ahad.controller;

import java.sql.SQLException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "/fragments/errors/error-page"; // error.html page show karega
    }

    @ExceptionHandler(SQLException.class)
    public String sqlException(SQLException ex, Model model) {
        model.addAttribute("errorMessage", "Database error: " + ex.getMessage());
        return "error"; // database-error.html page show karega
    }
}
