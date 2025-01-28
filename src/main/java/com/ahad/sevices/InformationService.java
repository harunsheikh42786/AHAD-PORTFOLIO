package com.ahad.sevices;

import java.security.Principal;

import com.ahad.entities.Information;

public interface InformationService {

    public void createInformation(Information information, Principal principal);

    public void updateInformation(Information information, Principal principal);

    public void deleteInformation(String id, Principal principal);

    public void getInformation(String id);

}
