package back_ptitulo.controller;

import back_ptitulo.dto.ArchivoDTO;
import back_ptitulo.service.ArchivosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/archivos")
public class ArchivosController {

    @Autowired
    private ArchivosService archivosService;

    @PostMapping
    public ResponseEntity<ArchivoDTO> uploadArchivo(@RequestBody Map<String, Object> requestBody) {
        Integer usuarioID = (Integer) requestBody.get("usuarioID");
        String contenido = (String) requestBody.get("contenido");
        String nombre = (String) requestBody.get("nombre");

        if (usuarioID == null || contenido == null || nombre == null) {
            return ResponseEntity.badRequest().build();
        }

        ArchivoDTO archivoDTO = archivosService.uploadArchivos(usuarioID, contenido, nombre);
        return ResponseEntity.ok(archivoDTO);
    }

    @GetMapping
    public ResponseEntity<List<ArchivoDTO>> getArchivosByUsuarioId(@RequestParam("usuarioID") Integer usuarioID) {
        return ResponseEntity.ok(archivosService.getArchivosByUsuarioId(usuarioID));
    }

    @GetMapping("/{archivoID}")
    public ResponseEntity<ArchivoDTO> getArchivoById(@PathVariable Integer archivoID) {
        return ResponseEntity.ok(archivosService.getArchivoById(archivoID));
    }

    @DeleteMapping("/{archivoID}")
    public ResponseEntity<String> deleteArchivoById(@PathVariable Integer archivoID) {
        archivosService.deleteArchivoById(archivoID);
        return ResponseEntity.ok("Archivo eliminado exitosamente");
    }


}
