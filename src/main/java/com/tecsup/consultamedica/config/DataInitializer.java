package com.tecsup.consultamedica.config;
import com.tecsup.consultamedica.model.Rol;
import com.tecsup.consultamedica.model.Usuario;
import com.tecsup.consultamedica.repository.RolRepository;
import com.tecsup.consultamedica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Crear roles mínimos exigidos por la rúbrica si no existen
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMINISTRADOR")
            .orElseGet(() -> rolRepository.save(new Rol("ROLE_ADMINISTRADOR", "Acceso total a todos los módulos y seguridad")));

        Rol rolMedico = rolRepository.findByNombre("ROLE_MEDICO")
            .orElseGet(() -> rolRepository.save(new Rol("ROLE_MEDICO", "Acceso a prescripción y recetas médicas")));

        Rol rolRecep = rolRepository.findByNombre("ROLE_RECEPCIONISTA")
            .orElseGet(() -> rolRepository.save(new Rol("ROLE_RECEPCIONISTA", "Acceso a inventario, lotes y medicamentos")));

        // 2. Crear usuarios semilla con contraseñas encriptadas en BCrypt
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombres("Administrador");
            admin.setApellidos("General");
            admin.setCorreo("admin@hospital.com");
            admin.setEstado("ACTIVO");
            admin.setRoles(Set.of(rolAdmin));
            usuarioRepository.save(admin);
        }

        if (!usuarioRepository.existsByUsername("medico1")) {
            Usuario medico = new Usuario();
            medico.setUsername("medico1");
            medico.setPassword(passwordEncoder.encode("medico123"));
            medico.setNombres("Carlos");
            medico.setApellidos("Ramírez");
            medico.setCorreo("carlos.ramirez@hospital.com");
            medico.setEstado("ACTIVO");
            medico.setRoles(Set.of(rolMedico));
            usuarioRepository.save(medico);
        }

        if (!usuarioRepository.existsByUsername("recepcion1")) {
            Usuario recep = new Usuario();
            recep.setUsername("recepcion1");
            recep.setPassword(passwordEncoder.encode("recep123"));
            recep.setNombres("María");
            recep.setApellidos("Gómez");
            recep.setCorreo("maria.gomez@hospital.com");
            recep.setEstado("ACTIVO");
            recep.setRoles(Set.of(rolRecep));
            usuarioRepository.save(recep);
        }
    }
}
