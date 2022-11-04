package com.devsuperior.dscatalog.dto;

import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

import java.io.Serializable;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
}
