package com.ahad.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ahad.entities.User;
import com.ahad.helper.Message;
import com.ahad.sevices.UserService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void modelHandler(Model model, Principal principal) {
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
            // User is authenticated
            model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        } else {
            // User is not authenticated
            model.addAttribute("user", new User());
        }
        model.addAttribute("message", new Message());
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-failed")
    public String loginFailed(Model model) {
        model.addAttribute("message", new Message("Check Email or Password. Data not found.", "danger"));
        return "login";
    }

    @GetMapping("/sign-up")
    public String registrationPage() {
        return "sign-up";
    }

    @PostMapping("/register")
    public String registration(@ModelAttribute User user, Model model) {
        User existedUser = this.userService.getUserByEmail(user.getEmail());
        if (existedUser != null) {
            model.addAttribute("message",
                    new Message("User already registered with this email. You can Sign-in", "success"));
            return "login";
        } else {
            User created = this.userService.createUser(user);
            if (created != null) {
                model.addAttribute("message", new Message("You Have successfully Registered.", "success"));
            } else {
                model.addAttribute("message", new Message("Sorry, For the inconvinience, Try Later.", "danger"));
            }
            return "sign-up";
        }
    }

}
