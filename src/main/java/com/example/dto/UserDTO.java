package com.example.dto;

import java.util.ArrayList;
import com.example.model.Account;

public record UserDTO(
    String userId,
    String name,
    String email,
    String direction,
    String cellphone,
    ArrayList<Account> accounts
) {}