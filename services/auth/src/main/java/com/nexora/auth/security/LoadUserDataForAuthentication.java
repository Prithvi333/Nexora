package com.nexora.auth.security;

import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class LoadUserDataForAuthentication implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByEmailAndEnabledTrue(userName);
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with email " + userName + "");
        }
        Users currentUser = user.get();
        Set<Roles> userRoles = currentUser.getRoles();
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        for (Roles role : userRoles) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return new User(userName, currentUser.getPassword(), grantedAuthorityList);

    }
}
