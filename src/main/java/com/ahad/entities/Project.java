package com.ahad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Project {

    @Id
    private String id;

    private String name; // Project name (e.g., "Inventory Management System")

    @Lob
    private String description; // Brief overview of the project

    @ElementCollection
    private List<String> technologies; // Technologies used (e.g., Java, Spring Boot, React)

    private LocalDate startDate; // Project start date
    private LocalDate endDate; // Project end date

    private boolean isOngoing; // Whether the project is currently ongoing

    @Lob
    private String responsibilities; // User's role and contributions

    private String status; // Status (e.g., "Completed", "In Progress", "On Hold")

    private String repositoryLink; // Link to GitHub/GitLab or similar repository
    private String demoLink; // Link to project demo (if hosted)

    @ManyToOne
    private User user; // Associated user (if part of a larger user-project relationship)

    @ElementCollection
    private List<String> teamMembers; // Team members involved (optional)
    @ElementCollection
    private List<String> imagePaths; // Team members involved (optional)

    // Getter and Setter methods for imagePaths
    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
