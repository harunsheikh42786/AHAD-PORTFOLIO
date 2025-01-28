package com.ahad.sevices.servicesImpl;

import java.security.Principal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ahad.entities.Information;
import com.ahad.entities.User;
import com.ahad.repos.InformationRepository;
import com.ahad.sevices.InformationService;
import com.ahad.sevices.UserService;

@Service
public class InformationSeviceImpl implements InformationService {

    @Autowired
    private UserService userService;
    @Autowired
    private InformationRepository informationRepository;

    @Override
    public void createInformation(Information information, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        information.setId(UUID.randomUUID().toString());
        information.setUser(user);
        user.setInformation(information);
        this.userService.updateUser(user, principal);
        this.informationRepository.save(information);
    }

    @Override
    public void updateInformation(Information information, Principal principal) {
        User user = this.userService.getUserByEmail(principal.getName());
        information.setId(user.getInformation().getId());
        information.setUser(user);
        user.setInformation(information);
        this.userService.updateUser(user, principal);
        this.informationRepository.save(information);
    }

    @Override
    public void deleteInformation(String id, Principal principal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteInformation'");
    }

    @Override
    public void getInformation(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInformation'");
    }

}
