package com.ahad.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Education {

    @Id
    private String id;

    private String degree; // Degree like B.Tech, M.Sc., etc.
    @Enumerated(EnumType.STRING)
    private DegreeType degreeType; // Enum for degree type (Full-time, Part-time, etc.)

    private String board; // Board name (e.g., University name or State Board)

    @Enumerated(EnumType.STRING)
    private CGPAType cgpaType; // Whether CGPA or percentage

    private Double cgpa; // CGPA if CGPAType is CGPA
    private Double percentage; // Percentage if CGPAType is Percentage

    private LocalDate startDate; // Graduation start date
    private LocalDate endDate; // Graduation end date

    @ManyToOne
    private User user;
}
