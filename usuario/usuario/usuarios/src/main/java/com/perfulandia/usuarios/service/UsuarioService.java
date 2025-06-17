package com.perfulandia.usuarios.service;

import com.perfulandia.usuarios.model.Usuario;
import com.perfulandia.usuarios.repository.UsuarioRepository;
import com.perfulandia.usuarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("Correo ya registrado");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // ✅ Método login con mensajes de depuración
    public String login(String correo, String password) {
        System.out.println("Intentando login con: " + correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("Usuario encontrado: " + usuario.getCorreo());
        System.out.println("Password en DB (encriptado): " + usuario.getPassword());
        System.out.println("Password ingresado: " + password);

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            System.out.println("❌ Contraseña incorrecta");
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtUtil.generarToken(correo);
        System.out.println("✅ Login exitoso, token generado: " + token);

        return token;
    }
}
