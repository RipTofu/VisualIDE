package back_ptitulo.repository;

import back_ptitulo.model.Centroestudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroEstudiosRepository extends JpaRepository<Centroestudio, Integer> {}
