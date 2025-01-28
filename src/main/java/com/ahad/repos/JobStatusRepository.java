package com.ahad.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.JobStatus;

public interface JobStatusRepository extends JpaRepository<JobStatus, String> {

    public boolean deleteJobStatusById(String id);

}
