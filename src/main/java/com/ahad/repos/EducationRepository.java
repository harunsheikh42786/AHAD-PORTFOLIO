package com.ahad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ahad.entities.Education;

public interface EducationRepository extends JpaRepository<Education, String> {

    @Modifying
    @Query("DELETE FROM Education e WHERE e.id = :id")
    void deleteEducationById(@Param("id") String id);

}
