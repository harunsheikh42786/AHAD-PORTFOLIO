package com.ahad.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ahad.entities.Project;
import com.ahad.entities.User;
import com.ahad.exeptions.HelperMessages;
import com.ahad.helper.FileService;
import com.ahad.helper.Message;
import com.ahad.sevices.ProjectService;
import com.ahad.sevices.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/portfolio")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
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

    @PostMapping("/add-project")
    public String createProject(@ModelAttribute Project project,
            @RequestParam("images1") MultipartFile[] images, Model model, Principal principal) {

        try {
            System.out.println("Number of images received: " + images.length); // Check how many images are received

            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile image : images) {
                System.out.println("Image name: " + image.getOriginalFilename()); // Log the image name
                if (!image.isEmpty()) {
                    try {
                        String imagePath = FileService.uploadImage("Project", image);
                        imagePaths.add(imagePath);
                    } catch (IOException e) {
                        model.addAttribute("message",
                                new Message("Failed to upload " + image.getOriginalFilename(), "danger"));
                        return "home";
                    }
                } else {
                    System.out.println("Image is empty: " + image.getOriginalFilename()); // Log if the image is empty
                }
            }

            project.setImagePaths(imagePaths);

            if (!project.getId().isEmpty()) {
                this.projectService.updateProjectByAddingImage(project);
                model.addAttribute("message", new Message(HelperMessages.PROJECT_UPDATE_SUCCESS, "success"));
            } else {
                this.projectService.createProject(project, principal);
                model.addAttribute("message", new Message(HelperMessages.PROJECT_ADD_SUCCESS, "success"));
            }

        } catch (Exception e) {
            model.addAttribute("message", new Message("An error occurred: " + e.getMessage(), "danger"));
        }

        return "add-details";
    }

    @GetMapping("/update-project/{id}")
    public String updateProjectPage(@PathVariable("id") String projectId, Model model, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());

        // Find project by ID
        Project project = user.getProjects()
                .stream()
                .filter(a -> a.getId().equals(projectId))
                .findFirst()
                .orElse(null); // Return null if project not found

        if (project == null) {
            model.addAttribute("message", new Message(HelperMessages.PROJECT_NOT_FOUND, "danger"));
            return "error-page"; // Redirect to error page or show error
        }

        // Add project to model
        model.addAttribute("project", project);
        return "add-details"; // Render "add-details" template
    }

    @DeleteMapping("/remove-project-image")
    public ResponseEntity<String> removeProjectImage(@RequestBody Map<String, String> requestBody,
            Principal principal) {
        try {
            String projectId = requestBody.get("projectId");
            String imagePath = requestBody.get("imagePath");

            // Fetch the project using the projectId
            Project project = this.userService.getUserByEmail(principal.getName()).getProjects().stream()
                    .filter(a -> a.getId().equals(projectId)).findFirst().orElse(null);

            if (project == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HelperMessages.PROJECT_NOT_FOUND);
            }

            // Remove the image from the list of image paths
            boolean isRemoved = project.getImagePaths().remove(imagePath);

            if (isRemoved) {
                FileService.deleteFile(imagePath);
                // Save the updated project to the database
                projectService.updateProjectByRemovingImage(project);
                return ResponseEntity.ok(HelperMessages.PROJECT_IMAGE_REMOVE_SUCCESS);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(HelperMessages.PROJECT_IMAGE_NOT_FOUND);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(HelperMessages.PROJECT_IMAGE_ERROR);
        }
    }

    @GetMapping("/remove-project/{id}")
    public String deleteProject(@PathVariable("id") String id, Model model, Principal principal) {
        int isRemoved = this.projectService.deleteProjectById(id, principal);
        if (isRemoved != -1) {
            model.addAttribute("message", new Message(HelperMessages.EDUCATION_REMOVE_SUCCESS, "success"));
        } else {
            model.addAttribute("message", new Message(HelperMessages.PROJECT_ERROR, "danger"));
        }
        return "add-details";
    }

    @GetMapping("/get-project/{id}")
    public String postMethodName(@PathVariable("id") String id, Model model, Principal principal) {
        Project foundProject = this.projectService.getProjectById(id);
        if (foundProject != null) {
            model.addAttribute("message", new Message(HelperMessages.PROJECT_ADD_SUCCESS, "success"));
        } else {
            model.addAttribute("message", new Message(HelperMessages.PROJECT_ERROR, "danger"));
        }
        return "add-details";
    }
}
