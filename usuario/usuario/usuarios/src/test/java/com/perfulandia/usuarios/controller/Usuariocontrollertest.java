package com.perfulandia.usuarios.controller;

import com.perfulandia.usuarios.model.LoginRequest;
import com.perfulandia.usuarios.model.Usuario;
import com.perfulandia.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Usuariocontrollertest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrar_Exito() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCorreo("test@example.com");
        usuario.setPassword("12345");

        when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuario);

        ResponseEntity<?> response = usuarioController.registrar(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());

        verify(usuarioService, times(1)).registrarUsuario(usuario);
    }

    @Test
    public void testRegistrar_Error() throws Exception {
    Usuario usuario = new Usuario();

    when(usuarioService.registrarUsuario(any(Usuario.class)))
        .thenThrow(new RuntimeException("Error al registrar"));

    ResponseEntity<?> response = usuarioController.registrar(usuario);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Error al registrar", response.getBody());

    verify(usuarioService, times(1)).registrarUsuario(usuario);
}

    @Test
    public void testLogin_Exito() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCorreo("test@example.com");
        request.setPassword("12345");

        when(usuarioService.login(request.getCorreo(), request.getPassword())).thenReturn("token123");

        ResponseEntity<?> response = usuarioController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token123", response.getBody());

        verify(usuarioService, times(1)).login(request.getCorreo(), request.getPassword());
    }

    @Test
    public void testLogin_Error() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setCorreo("test@example.com");
        request.setPassword("12345");

        // Simula error con RuntimeException en vez de Exception
        when(usuarioService.login(request.getCorreo(), request.getPassword()))
            .thenThrow(new RuntimeException("Credenciales inválidas"));

        ResponseEntity<?> response = usuarioController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody());

        verify(usuarioService, times(1)).login(request.getCorreo(), request.getPassword());
    }
}
