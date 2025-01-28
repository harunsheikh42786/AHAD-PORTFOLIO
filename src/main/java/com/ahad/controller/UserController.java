package com.ahad.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ahad.entities.Information;
import com.ahad.entities.User;
import com.ahad.helper.FileService;
import com.ahad.helper.Message;
import com.ahad.sevices.InformationService;
import com.ahad.sevices.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InformationService informationService;

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

    @GetMapping("/add-details")
    public String addDetails() {
        return "add-details";
    }

    @PostMapping("/update-profile")
    public String updateUser(@ModelAttribute User user, @RequestParam("image") MultipartFile image, Model model,
            Principal principal) {
        try {
            if (!image.isEmpty()) {
                try {
                    String imagePath = FileService.uploadImage("User", image);
                    FileService.deleteFile(user.getImagePath());
                    user.setImagePath(imagePath);
                } catch (IOException e) {
                    model.addAttribute("message",
                            new Message("Failed to upload " + image.getOriginalFilename(), "danger"));
                    return "user-profile";
                }
            }
            this.userService.updateUser(user, principal);
            model.addAttribute("message", new Message("User Profile Update Successfully.", "success"));
        } catch (Exception e) {
            model.addAttribute("message", new Message("Error : " + e.getMessage(), "danger"));
        }
        return "user-profile";
    }

    @PostMapping("/update-information")
    public String updateUserInformation(@ModelAttribute Information information, Model model,
            Principal principal) {
        try {
            if (information.getId() == null || information.getId().isEmpty()) {
                this.informationService.createInformation(information, principal);
            } else {
                this.informationService.updateInformation(information, principal);
            }
            model.addAttribute("message", new Message("User Information Update Successfully.", "success"));
        } catch (Exception e) {
            model.addAttribute("message", new Message("Error : " + e.getMessage(), "danger"));
        }
        return "user-profile";
    }

    @GetMapping("/portfolio")
    public String portFolio() {
        return "portfolio";
    }

    @GetMapping("/profile")
    public String userProfile() {
        return "user-profile";
    }

    @GetMapping("/remove-resume")
    public String removeResume(Model model, Principal principal) {
        try {

            // Fetch the user using the logged-in user's email
            User user = this.userService.getUserByEmail(principal.getName());
            FileService.deleteFile(user.getResume()); // Ensure this method handles the file path properly
            user.setResume(null);
            this.userService.updateUser(user, principal);
            model.addAttribute("message", new Message("Resume deleted successfully.", "success"));

        } catch (Exception e) {
            // Add an error message to the model in case of any exception
            model.addAttribute("message", new Message("Error during resume deletion: " + e.getMessage(), "danger"));
        }

        // Return the view name (adjust according to the actual view you want to return)
        return "add-details"; // Make sure 'add-details' corresponds to your actual template
    }

    @PostMapping("/upload-resume")
    public String uploadResume(@RequestParam("resume") MultipartFile file, Model model, Principal principal) {
        try {
            // Upload the resume file and get the file name
            String fileName = FileService.uploadImage("Resume", file); // Corrected method name

            // Pass the file name to the service layer for saving it to the user
            this.userService.uploadResume(fileName, principal);

            model.addAttribute("message", new Message("Resume uploaded successfully.", "success"));
        } catch (Exception e) {
            model.addAttribute("message", new Message("Error during resume uploading: " + e.getMessage(), "danger"));
        }
        return "add-details"; // Returning the view name
    }

}
