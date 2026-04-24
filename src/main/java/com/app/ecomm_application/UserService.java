package com.app.ecomm_application;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final List<User> userList=new ArrayList<>();


    public List<User> fetchAllUsers(){
        return userList;
    }

    public Optional<User> fetchUser(Long id){
        return userList.stream()
                .filter(user->user.getId().equals(id))
                .findFirst();


    }

    public boolean updateUser(Long id, User updateDetails){
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(user -> {
                    user.setFirstName(updateDetails.getFirstName());
                    user.setLastName(updateDetails.getLastName());
                    return true;
                })
                .orElse(false);
    }

    public void addUser(User user){
        userList.add(user);
    }
}
