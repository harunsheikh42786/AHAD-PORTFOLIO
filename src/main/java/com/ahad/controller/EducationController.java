package com.ahad.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ahad.entities.Education;
import com.ahad.entities.User;
import com.ahad.helper.Message;
import com.ahad.sevices.EducationService;
import com.ahad.sevices.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/portfolio")
public class EducationController {

    @Autowired
    private UserService userService;
    @Autowired
    private EducationService educationService;

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

    @PostMapping("/add-education")
    public String createEducation(@ModelAttribute Education education, Model model, Principal principal) {
        try {
            if (!education.getId().isEmpty()) {
                this.educationService.updateEducation(education, principal);
            } else {
                this.educationService.createEducation(education, principal);
            }

            model.addAttribute("message", new Message("Education Successfully Added.", "success"));
        } catch (Exception e) {
            model.addAttribute("message", new Message("An error occurred: " + e.getMessage(), "danger"));
        }

        return "add-details";
    }

    @GetMapping("/update-education/{id}")
    public String updateEducation(@PathVariable("id") String educationId, Model model, Principal principal) {
        Education education = this.educationService.getEducation(educationId);
        model.addAttribute("education", education);
        return "add-details";
    }

    @GetMapping("/remove-education/{id}")
    public String deleteEducation(@PathVariable("id") String educationId, Model model, Principal principal) {
        boolean isDeleted = this.educationService.deleteEducation(educationId, principal);
        if (isDeleted) {
            model.addAttribute("message", new Message("Education removed Successfully.", "success"));
        } else {
            model.addAttribute("message", new Message("Error in removing education.", "danger"));
        }
        return "add-details";
    }

    @GetMapping("/get-education/{id}")
    public String getEducation() {
        return "add-details";
    }

}
