package com.ahad.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {
    public boolean deleteProjectById(String id);
}
