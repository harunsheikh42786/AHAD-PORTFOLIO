package com.ahad.sevices;

import java.security.Principal;

import com.ahad.entities.Project;

public interface ProjectService {

    public Project createProject(Project project, Principal principal);

    public Project updateProjectByAddingImage(Project project);

    public Project updateProjectByRemovingImage(Project project);

    public Project getProjectById(String id);

    public int deleteProjectById(String id, Principal principal);

}
