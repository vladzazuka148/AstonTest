package com.example.astontest.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequestDto {
    private String name;
    private String pincode;
}
