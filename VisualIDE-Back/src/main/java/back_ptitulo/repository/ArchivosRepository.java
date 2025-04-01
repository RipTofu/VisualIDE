package back_ptitulo.repository;

import back_ptitulo.model.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchivosRepository extends JpaRepository<Archivo, Integer> {
    List<Archivo> findByUsuario_Id(Integer usuarioId);  // Fixed method name
}
