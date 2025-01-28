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
public class JobStatus {

    @Id
    private String id;

    private String position; // Job title (e.g., Software Developer, Data Scientist)
    private String company; // Company name
    private String location; // Job location (e.g., City, Country)
    private String jobType; // Full-time, Part-time, Internship, Contract, etc.

    private LocalDate startDate; // Job start date
    private LocalDate endDate; // Job end date (null if currently employed)

    @Lob
    private String responsibilities; // Key job responsibilities
    private Double salary; // User's salary (optional)
    private boolean isCurrentJob; // Whether this is the user's current job
    private boolean isFresher; // True if the user is a fresher (no prior job experience)

    @ElementCollection
    private List<String> imagePaths;

    @ElementCollection
    private List<String> skills; // List of skills learned in this job

    @ElementCollection
    private List<String> projects; // List of major projects or accomplishments

    @ElementCollection
    private List<String> certifications; // Certifications earned during the job

    @ManyToOne
    private User user;

    // Getter and Setter methods for imagePaths
    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
