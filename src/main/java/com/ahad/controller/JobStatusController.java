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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ahad.entities.JobStatus;
import com.ahad.entities.User;
import com.ahad.helper.FileService;
import com.ahad.helper.Message;
import com.ahad.sevices.JobStatusService;
import com.ahad.sevices.UserService;

@Controller
@RequestMapping("/portfolio")
public class JobStatusController {

    @Autowired
    private JobStatusService jobStatusService;
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

    @PostMapping("/add-job-status")
    public String createJobStatus(@ModelAttribute JobStatus jobStatus,
            @RequestParam("images2") MultipartFile[] images, Model model, Principal principal) {
        try {
            System.out.println("Number of images received: " + images.length); // Check how many images are received

            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile image : images) {
                System.out.println("Image name: " + image.getOriginalFilename()); // Log the image name
                if (!image.isEmpty()) {
                    try {
                        String imagePath = FileService.uploadImage("JobStatus", image);
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
            jobStatus.setImagePaths(imagePaths);
            if (!jobStatus.getId().isEmpty()) {
                this.jobStatusService.updateJobStatusByAddingImages(jobStatus, principal);
                model.addAttribute("message", new Message("JobStatus Successfully Updated.", "success"));
            } else {
                this.jobStatusService.createJobStatus(jobStatus, principal);
                model.addAttribute("message", new Message("JobStatus Successfully Added.", "success"));
            }

        } catch (Exception e) {
            model.addAttribute("message", new Message("An error occurred: " + e.getMessage(), "danger"));
        }

        return "add-details";
    }

    @GetMapping("/update-job-status/{id}")
    public String updateJobPage(@PathVariable("id") String jobStatusId, Model model, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        JobStatus existedJobStatus = user.getJobStatuses().stream().filter(a -> a.getId().equals(jobStatusId))
                .findFirst().orElse(null);
        model.addAttribute("jobStatus", existedJobStatus);
        return "add-details";
    }

    @PostMapping("/update-job-status")
    public String updateJobStatus(@ModelAttribute("JobStatus") JobStatus jobStatus, Model model, Principal principal) {
        JobStatus createdJobStatus = this.jobStatusService.updateJobStatusByAddingImages(jobStatus, principal);
        if (createdJobStatus != null) {
            model.addAttribute("message", new Message("JobStatus Successfully Updated.", "success"));
        } else {
            model.addAttribute("message", new Message("Sorry, For the inconvinience.", "danger"));
        }
        return "add-details";
    }

    @GetMapping("/remove-job-status/{id}")
    public String deleteJobStatus(@PathVariable("id") String jobStatusId, Model model, Principal principal) {
        boolean isRemoved = this.jobStatusService.deleteJobStatusById(jobStatusId, principal);
        if (isRemoved) {
            model.addAttribute("message", new Message("JobStatus Removed Successfully.", "success"));
        } else {
            model.addAttribute("message", new Message("Sorry, For the inconvinience.", "danger"));
        }
        return "add-details";
    }

    @GetMapping("/get-job-status/{id}")
    public String postMethodName(@PathVariable("id") String id, Model model, Principal principal) {
        JobStatus foundJobStatus = this.jobStatusService.getJobStatus(id);
        if (foundJobStatus != null) {
            model.addAttribute("message", new Message("JobStatus Successfully Added.", "success"));
        } else {
            model.addAttribute("message", new Message("Sorry, For the inconvinience.", "danger"));
        }
        return "add-details";
    }

    @DeleteMapping("/remove-job-status-image")
    public ResponseEntity<String> removeProjectImage(@RequestBody Map<String, String> requestBody,
            Principal principal) {
        try {
            String jobStatusId = requestBody.get("jobStatusId");
            String imagePath = requestBody.get("imagePath");

            // Null checks for inputs
            if (jobStatusId == null || imagePath == null) {
                return ResponseEntity.badRequest().body("JobStatus ID or Image Path is missing.");
            }

            // Fetch the jobStatus using the jobStatusId
            JobStatus jobStatus = this.userService.getUserByEmail(principal.getName())
                    .getJobStatuses()
                    .stream()
                    .filter(a -> a.getId().equals(jobStatusId))
                    .findFirst()
                    .orElse(null);

            if (jobStatus == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("JobStatus not found.");
            }

            // Remove the image from the list of image paths
            boolean isRemoved = jobStatus.getImagePaths().remove(imagePath);

            if (isRemoved) {
                FileService.deleteFile(imagePath);
                // Save the updated jobStatus to the database
                this.jobStatusService.updateJobStatusByRmovingImages(jobStatus, principal);
                return ResponseEntity.ok("Image removed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found in the jobStatus.");
            }
        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing image.");
        }
    }

}
