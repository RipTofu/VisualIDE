package back_ptitulo.controller;

import back_ptitulo.model.Usuario;
import back_ptitulo.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController {

    @Autowired
    private UsuariosService usuariosService;

    /**
     * Endpoint to register a new user.
     * @param userDetails User registration details (excluding CentroID).
     * @return Response indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Usuario userDetails) {
        System.out.println("Valores recibidos: " + userDetails);

        try {
            // Register the user
            usuariosService.registerUsuario(
                    userDetails.getEmail(),
                    userDetails.getContrasena(),
                    userDetails.getNombre()
            );

            return ResponseEntity.ok("Registration successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred");
        }
    }
}
