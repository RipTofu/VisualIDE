package back_ptitulo.controller;

import back_ptitulo.DTO.LoginDTO;
import back_ptitulo.DTO.UserResponseDTO;
import back_ptitulo.model.Usuario;
import back_ptitulo.service.JwtService;
import back_ptitulo.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginCredentials) {
        System.out.println("Login attempt: email = " + loginCredentials.getEmail());
        System.out.println("Login attempt: password = " + loginCredentials.getContrasena());

        // Find the user by email
        Usuario user = usuariosService.findByEmail(loginCredentials.getEmail());
        if (user != null) {
            System.out.println("User found: " + user);
            if (user.getContrasena().equals(loginCredentials.getContrasena())) {
                String token = jwtService.generateToken(user.getEmail());
                System.out.println("Token generated: " + token);

                UserResponseDTO response = new UserResponseDTO();
                response.setNombre(user.getNombre());
                response.setEmail(user.getEmail());
                response.setToken(token);
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Invalid password");
            }
        } else {
            System.out.println("User not found");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
