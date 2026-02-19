package com.cmatos.portfolio_backend_api.services;

import com.cmatos.portfolio_backend_api.model.enums.UserRole;
import com.cmatos.portfolio_backend_api.records.UserDTO;
import com.cmatos.portfolio_backend_api.model.User;
import com.cmatos.portfolio_backend_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public void save(UserDTO dto) {
        User user = new User();
        user.setUsernameExibition(dto.username());
        user.setPassword(bCryptPasswordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRole(UserRole.USER);
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsernameExibition(username);
    }

    public User findUserByEmail(String username) {
        return userRepository.findUserByEmail(username);
    }

    public Boolean verifyExistingUser(UserDTO dto) {
        User userByUsername = findUserByUsername(dto.username());
        User userByEmail = findUserByEmail(dto.email());

        return userByUsername != null || userByEmail != null;
    }
}
