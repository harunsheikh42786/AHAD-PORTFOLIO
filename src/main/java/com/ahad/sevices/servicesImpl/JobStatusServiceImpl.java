package com.ahad.sevices.servicesImpl;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.JobStatus;
import com.ahad.entities.User;
import com.ahad.repos.JobStatusRepository;
import com.ahad.sevices.JobStatusService;
import com.ahad.sevices.UserService;

import jakarta.transaction.Transactional;

@Service
public class JobStatusServiceImpl implements JobStatusService {

    @Autowired
    private JobStatusRepository jobStatusRepository;
    @Autowired
    private UserService userService;

    @Override
    public JobStatus createJobStatus(JobStatus jobStatus, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        // Check if the jobStatus ID is already set
        jobStatus.setId(UUID.randomUUID().toString());
        // Associate the jobStatus with the user
        jobStatus.setUser(user);
        // Avoid duplicate jobStatus entries in the user's list
        user.getJobStatuses().add(jobStatus);
        this.userService.updateUser(user, principal);
        // Save the jobStatus and return
        return this.jobStatusRepository.saveAndFlush(jobStatus);
    }

    @Override
    public JobStatus updateJobStatusByAddingImages(JobStatus jobStatus, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        JobStatus existedJobStatus = user.getJobStatuses().stream().filter(a -> a.getId().equals(jobStatus.getId()))
                .findFirst().orElse(null);
        if (!jobStatus.getImagePaths().isEmpty()) {
            jobStatus.getImagePaths().addAll(existedJobStatus.getImagePaths());
        }
        jobStatus.setUser(user);
        return this.jobStatusRepository.save(jobStatus);
    }

    @Override
    public JobStatus updateJobStatusByRmovingImages(JobStatus jobStatus, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        JobStatus existedJobStatus = user.getJobStatuses().stream().filter(a -> a.getId().equals(jobStatus.getId()))
                .findFirst().orElse(null);
        jobStatus.setUser(user);
        return this.jobStatusRepository.save(jobStatus);
    }

    @Override
    @Transactional
    public boolean deleteJobStatusById(String id, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        JobStatus jobStatus = this.jobStatusRepository.findById(id).get();
        user.getJobStatuses().remove(jobStatus);
        this.userService.updateUser(user, principal);
        jobStatusRepository.delete(jobStatus);
        return true;
    }

    @Override
    public JobStatus getJobStatus(String id) {
        return jobStatusRepository.findById(id).get();
    }

}
