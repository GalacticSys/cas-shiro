package com.example.casshiro.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActive {
    private User user;
    private List<Permission> permissionLsit;
    private List<Role> roleList;


}
