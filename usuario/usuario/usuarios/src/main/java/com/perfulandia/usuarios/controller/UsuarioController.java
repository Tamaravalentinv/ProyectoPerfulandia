package com.perfulandia.usuarios.controller;

import com.perfulandia.usuarios.model.LoginRequest;
import com.perfulandia.usuarios.model.Usuario;
import com.perfulandia.usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Método para listar usuarios SIN HATEOAS (JSON plano)
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Usuario>> listarUsuariosPlano() {
        List<Usuario> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Método para listar usuarios CON HATEOAS (HAL+JSON)
    @GetMapping(produces = "application/hal+json")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.obtenerTodos().stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).obtenerUsuario(usuario.getId())).withSelfRel(),
                        // Link al endpoint plano para evitar confusión con media types
                        linkTo(methodOn(UsuarioController.class).listarUsuariosPlano()).withRel("usuarios")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Usuario>> collectionModel = CollectionModel.of(usuarios,
                linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    // Método para obtener usuario por id SIN HATEOAS (JSON plano)
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Usuario> obtenerUsuarioPlano(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    // Método para obtener usuario por id CON HATEOAS (HAL+JSON)
    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<Usuario> resource = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).obtenerUsuario(id)).withSelfRel(),
                // Link al listado plano para evitar confusión
                linkTo(methodOn(UsuarioController.class).listarUsuariosPlano()).withRel("usuarios"));
        return ResponseEntity.ok(resource);
    }

    // Registro de nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario registrado = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(registrado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Login y generación de token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = usuarioService.login(request.getCorreo(), request.getPassword());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
