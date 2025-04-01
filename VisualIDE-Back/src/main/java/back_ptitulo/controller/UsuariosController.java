package back_ptitulo.controller;

import back_ptitulo.DTO.UsuarioDTO;
import back_ptitulo.model.Usuario;
import back_ptitulo.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioDTO> getPerfil(@RequestParam String email) {
        System.out.println("Cargando datos de perfil...");
        Usuario usuario = usuariosService.findByEmail(email);
        System.out.println("Retornando info de usuario: " + usuario);

        if (usuario != null) {
            // Convert Usuario to UsuarioDTO
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setUsuarioID(usuario.getId());
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setNombre(usuario.getNombre());
            usuarioDTO.setAlmacenamientodisponible(usuario.getAlmacenamientodisponible());
            usuarioDTO.setArchivosguardados(usuario.getArchivosguardados());

            // Return the UsuarioDTO as a JSON response
            return ResponseEntity.ok(usuarioDTO);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}