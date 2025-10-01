package com.application.mybillbook.service;

import com.application.mybillbook.model.Users;
import com.application.mybillbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Accept username OR email. Returns a UserDetails with the stored (BCrypt) password.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Users user = userRepository.findByUserName(usernameOrEmail.toLowerCase());
        if (user == null) {
            user = userRepository.findByEmail(usernameOrEmail);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }

        // Build Spring Security user. Grant a default role USER.
        return User.builder()
                .username(user.getUserName() != null ? user.getUserName() : user.getEmail())
                .password(user.getPassword()) // password stored must be BCrypt hash
                .roles("USER")
                .build();
    }
}
