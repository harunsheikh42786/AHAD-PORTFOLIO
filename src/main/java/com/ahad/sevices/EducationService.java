package com.ahad.sevices;

import java.security.Principal;

import com.ahad.entities.Education;

public interface EducationService {

    public Education createEducation(Education education, Principal principal);

    public Education updateEducation(Education education, Principal principal);

    public boolean deleteEducation(String id, Principal principal);

    public Education getEducation(String id);

}
