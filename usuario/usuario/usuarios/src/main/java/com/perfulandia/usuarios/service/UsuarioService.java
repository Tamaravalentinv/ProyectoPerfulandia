package com.perfulandia.usuarios.service;

import com.perfulandia.usuarios.model.Usuario;
import com.perfulandia.usuarios.repository.UsuarioRepository;
import com.perfulandia.usuarios.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;  // Inyecta tu clase JwtUtil

    public Usuario registrarUsuario(Usuario usuario) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(usuario.getCorreo());

        if (existente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        return usuarioRepository.save(usuario);
    }

    public String login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Genera y devuelve el token JWT real usando JwtUtil
        return jwtUtil.generarToken(correo);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
