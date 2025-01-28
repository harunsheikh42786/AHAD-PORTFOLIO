package com.ahad.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahad.entities.Information;

public interface InformationRepository extends JpaRepository<Information, String> {

}
