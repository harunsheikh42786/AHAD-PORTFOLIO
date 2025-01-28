package com.ahad.sevices.servicesImpl;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.Project;
import com.ahad.entities.User;
import com.ahad.repos.ProjectRepository;
import com.ahad.sevices.ProjectService;
import com.ahad.sevices.UserService;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project createProject(Project project, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        // Check if the project ID is already set
        project.setId(UUID.randomUUID().toString());
        // Associate the project with the user
        project.setUser(user);
        // Avoid duplicate project entries in the user's list
        user.getProjects().add(project);
        this.userService.updateUser(user, principal);
        // Save the project and return
        return this.projectRepository.save(project);
    }

    @Override
    public Project updateProjectByAddingImage(Project project) {
        Project existedProject = this.projectRepository.findById(project.getId()).get();
        project.getImagePaths().addAll(existedProject.getImagePaths());
        project.setUser(existedProject.getUser());
        return this.projectRepository.save(project);
    }

    @Override
    public Project updateProjectByRemovingImage(Project project) {
        Project existedProject = this.projectRepository.findById(project.getId()).get();
        project.setUser(existedProject.getUser());
        return this.projectRepository.save(project);
    }

    @Override
    public Project getProjectById(String id) {
        return this.projectRepository.findById(id).get();
    }

    @Override
    public int deleteProjectById(String id, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        Project project = this.projectRepository.findById(id).get();
        if (project != null) {
            user.getProjects().remove(project);
            this.userService.updateUser(user, principal);
            this.projectRepository.deleteById(id);
            return 1;
        }
        return -1;
    }

}
