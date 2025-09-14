package com.application.mybillbook;

import com.application.mybillbook.model.Users;
import com.application.mybillbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    // Show the form
    @GetMapping("/register-form")
    public String showForm() {
        return "register";  // refers to register.html
    }

    @GetMapping("/check-email")
    @ResponseBody
    public String checkEmail(@RequestParam String email) {
        Users existingUser = userRepository.findByEmail(email);
        if(existingUser != null){
            return "taken";
        }
        else{
            return "Available";
        }
    }

    @GetMapping("/check-username")
    @ResponseBody
    public String checkUsername(@RequestParam String userName) {
        Users existingUser = userRepository.findByUserName(userName);
        if (existingUser != null) {
            return "taken";
        } else {
            return "available";
        }
    }

    // Handle form submission
    @PostMapping("/register")
    @ResponseBody
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String userName,
            @RequestParam String password
    ) {
        Users existingUser = userRepository.findByUserName(userName);
        if (existingUser != null) {
            return "Error: Username '" + userName + "' is already taken.";
        }
        try {
            Users user = new Users(firstName, lastName, email, userName, password);
            userRepository.save(user);
            return "User registered: " + userName;
        } catch (DuplicateKeyException e) {
            return "Error: Username already exists (duplicate key).";
        }
    }
}
