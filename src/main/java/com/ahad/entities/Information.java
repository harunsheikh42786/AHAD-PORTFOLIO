package com.ahad.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Information {

    @Id
    private String id;

    private String contactNumber; // User's contact number
    private String linkedIn; // LinkedIn profile URL
    private String github; // GitHub profile URL
    private String address; // User's address

    @Column(length = 1000)
    private String description;

    private boolean isPublic; // Whether user wants to make this information public or private
    @OneToOne
    private User user;
}
