package com.app.ecomm_application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> fetchAllUsers(){
        return userRepository.findAll();

    }

    public Optional<User> fetchUser(Long id){
        return userRepository.findById(id);
    }

    public boolean updateUser(Long id, User updateDetails){
        return userRepository.findById(id)
                .map(user -> {
                user.setFirstName(updateDetails.getFirstName());
                user.setLastName(updateDetails.getLastName());
                userRepository.save(user);
                return true;
                })
                .orElse(false);
    }

    public void addUser(User user){
        userRepository.save(user);
    }
}
