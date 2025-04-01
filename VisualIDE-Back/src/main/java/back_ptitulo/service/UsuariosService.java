package back_ptitulo.service;

import back_ptitulo.model.Centroestudio;
import back_ptitulo.model.Usuario;
import back_ptitulo.repository.CentroEstudiosRepository;
import back_ptitulo.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private CentroEstudiosRepository centroEstudiosRepository;

    public Usuario registerUsuario(String email, String contrasena, String nombre) {
        if (usuariosRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        int defaultCentroID = 1;
        Centroestudio centro = centroEstudiosRepository.findById(defaultCentroID).orElse(null);

        if (centro == null) {
            centro = new Centroestudio();
            centro.setId(defaultCentroID);
            centro.setNombre("Universidad Andrés Bello");
            centroEstudiosRepository.save(centro);
            System.out.println("CentroEstudio with ID 1 created: " + centro); // Log the creation of the CentroEstudio
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
        usuario.setNombre(nombre);
        usuario.setCentroid(centro);
        usuario.setAlmacenamientodisponible(60);
        usuario.setArchivosguardados(0);

        return usuariosRepository.save(usuario);
    }


    public Usuario findByEmail(String email) {
        return usuariosRepository.findByEmail(email);
    }

    public boolean validateUserCredentials(String email, String contrasena) {
        return true;
    }
}
