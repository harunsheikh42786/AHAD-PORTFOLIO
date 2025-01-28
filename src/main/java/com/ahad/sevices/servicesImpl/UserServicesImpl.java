package com.ahad.sevices.servicesImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ahad.entities.User;
import com.ahad.helper.FileService;
import com.ahad.repos.UserRepository;
import com.ahad.sevices.UserService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServicesImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setLocalDateTime(LocalDateTime.now());
        user.setEnable(true);
        user.setRole("NORMAL");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Principal principal) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(String email) {
        this.userRepository.deleteUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findUserByEmail(email);
    }

    @Override
    public User getUserById(String id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public boolean uploadResume(String fileName, Principal principal) {
        // Use the principal to fetch the logged-in user
        String userEmail = principal.getName(); // The email of the logged-in user
        User user = this.userRepository.findUserByEmail(userEmail); // Fetch the user by email

        if (user != null) {
            // Check if the user already has a resume and delete it
            if (user.getResume() != null && !user.getResume().isEmpty()) {
                // Delete the old resume file
                FileService.deleteFile(user.getResume());
            }

            // Set the new resume file name
            user.setResume(fileName);

            // Save the updated user to the database
            User updatedUser = this.userRepository.save(user);

            return updatedUser != null; // Return true if the user was successfully updated
        }

        return false; // Return false if the user was not found
    }

}