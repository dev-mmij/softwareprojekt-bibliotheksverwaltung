package com.softwareprojekt.gruppe8.security;

import com.softwareprojekt.gruppe8.data.UserRepository;
import com.softwareprojekt.gruppe8.konto.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Kein Benutzer mit dem Benutzernamen vorhanden: " + username
                        )
                );

        if (user.getRolle() == null) {
            throw new UsernameNotFoundException(
                    "Für den Benutzer ist keine Rolle hinterlegt: " + username
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getHashedPassword(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRolle().getName().name()
                        )
                )
        );
    }
}