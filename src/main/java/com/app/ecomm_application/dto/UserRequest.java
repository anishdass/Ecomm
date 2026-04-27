package com.app.ecomm_application.dto;

import com.app.ecomm_application.model.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private AddressDTO address;
}
