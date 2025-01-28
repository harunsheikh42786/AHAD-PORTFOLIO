package com.ahad.sevices.servicesImpl;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import com.ahad.entities.Education;
import com.ahad.entities.User;
import com.ahad.repos.EducationRepository;
import com.ahad.sevices.EducationService;
import com.ahad.sevices.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class EducationServiceImpl implements EducationService {

    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private UserService userService;

    @Override
    public Education createEducation(Education education, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        education.setId(UUID.randomUUID().toString());
        education.setUser(user);
        user.getEducation().add(education);
        this.userService.updateUser(user, principal);
        return this.educationRepository.save(education);
    }

    @Override
    public Education updateEducation(Education education, Principal principal) {
        Education existedEducation = this.educationRepository.findById(education.getId()).get();
        education.setUser(existedEducation.getUser());
        return this.educationRepository.save(education);
    }

    @Override
    @Modifying
    @Transactional
    public boolean deleteEducation(String id, Principal principal) {
        Education education = this.educationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Education not found"));
        User user = userService.getUserByEmail(principal.getName());
        user.getEducation().remove(education);
        this.userService.updateUser(user, principal);
        educationRepository.delete(education); // Use delete(entity) for better transaction handling
        return true;
    }

    @Override
    public Education getEducation(String id) {
        return this.educationRepository.findById(id).get();
    }

}
