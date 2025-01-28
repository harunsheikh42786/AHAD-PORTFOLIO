package com.ahad.sevices;

import java.security.Principal;

import com.ahad.entities.User;

public interface UserService {

    public User createUser(User user);

    public User updateUser(User user, Principal principal);

    public void deleteUser(String email);

    public User getUserByEmail(String email);

    public User getUserById(String id);

    public boolean uploadResume(String fileName, Principal principal);

}
