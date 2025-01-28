package com.ahad.sevices;

import java.security.Principal;

import com.ahad.entities.JobStatus;

public interface JobStatusService {

    public JobStatus createJobStatus(JobStatus jobStatus, Principal principal);

    public JobStatus updateJobStatusByAddingImages(JobStatus jobStatus, Principal principal);

    public JobStatus updateJobStatusByRmovingImages(JobStatus jobStatus, Principal principal);

    public boolean deleteJobStatusById(String id, Principal principal);

    public JobStatus getJobStatus(String id);

}
