package com.app.ecomm_application.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String city;
    private String country;
    private String state;
    private String street;
    private String zipcode;
}
