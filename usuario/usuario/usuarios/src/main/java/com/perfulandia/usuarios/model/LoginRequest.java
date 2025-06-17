package com.perfulandia.usuarios.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String correo;
    private String password;
}
