package com.employee.management.system.security;

import com.employee.management.system.entity.UserEntity;
import com.employee.management.system.exception.NotFoundException;
import com.employee.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByUsernameWithRoles(username).orElseThrow(()
                -> new NotFoundException("User not found"));
        System.out.println("USING WITH ROLES METHOD");


        Set<GrantedAuthority> grantedAuthorities = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())).collect(Collectors.toSet());

        System.out.println("USERNAME = " + username);
        System.out.println("ROLES SIZE = " + user.getRoles().size());
        System.out.println("ROLES = " + user.getRoles());
        System.out.println("AUTHORITIES = " + grantedAuthorities);

        return new User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);


    }
}
