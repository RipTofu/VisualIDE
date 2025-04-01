package back_ptitulo.service;

import back_ptitulo.dto.ArchivoDTO;
import back_ptitulo.model.Archivo;
import back_ptitulo.model.Usuario;
import back_ptitulo.repository.ArchivosRepository;
import back_ptitulo.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArchivosService {

    @Autowired
    private ArchivosRepository archivosRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    public List<ArchivoDTO> getArchivosByUsuarioId(Integer usuarioID) {
        List<ArchivoDTO> archivos = archivosRepository.findByUsuario_Id(usuarioID).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        archivos.forEach(dto -> System.out.println("ArchivoDTO: " + dto.getId() + ", " + dto.getNombre()));
        return archivos;
    }

    public ArchivoDTO getArchivoById(Integer archivoID) {
        Archivo archivo = archivosRepository.findById(archivoID)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado"));
        ArchivoDTO dto = toDTO(archivo);
        System.out.println("ArchivoDTO: " + dto.getId() + ", " + dto.getNombre());
        return dto;
    }

    public ArchivoDTO uploadArchivos(Integer usuarioID, String contenido, String nombre) {
        Usuario usuario = usuariosRepository.findById(usuarioID)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Calculate file size in KB
        int tamano = (contenido.getBytes().length + 1023) / 1024; // Round up to nearest KB

        Archivo archivo = new Archivo();
        archivo.setUsuario(usuario);
        archivo.setContenido(contenido);
        archivo.setNombre(nombre);
        archivo.setFecha(Instant.now());
        archivo.setTamano(tamano);

        Archivo savedArchivo = archivosRepository.save(archivo);
        return toDTO(savedArchivo);
    }

    /**
     * Map an Archivo entity to ArchivoDTO.
     *
     * @param archivo The Archivo entity.
     * @return The ArchivoDTO object.
     */
    private ArchivoDTO toDTO(Archivo archivo) {
        return new ArchivoDTO(
                archivo.getId(),
                archivo.getNombre(),
                archivo.getContenido(), // You can omit this if it's too large for certain endpoints
                archivo.getFecha(),
                archivo.getTamano()
        );
    }

    public void deleteArchivoById(Integer archivoID) {
        Archivo archivo = archivosRepository.findById(archivoID)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado"));
        archivosRepository.delete(archivo);
    }

}
