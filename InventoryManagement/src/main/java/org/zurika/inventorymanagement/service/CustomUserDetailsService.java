package org.zurika.inventorymanagement.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.zurika.inventorymanagement.model.User;
import org.zurika.inventorymanagement.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException
        ("User not found: " + username));

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.getLastName()))
            .collect(Collectors.toList())
            );
    }
}
