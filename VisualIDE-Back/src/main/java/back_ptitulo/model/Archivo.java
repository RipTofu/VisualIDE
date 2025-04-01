package back_ptitulo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ARCHIVOS")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARCHIVOID", nullable = false)
    private Integer id;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USUARIOID", nullable = false)
    private Usuario usuario;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "FECHA", nullable = false)
    private Instant fecha;

    @NotNull
    @Lob
    @Column(name = "CONTENIDO", nullable = false)
    private String contenido;

    @Size(max = 255)
    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "TAMANO")
    private Integer tamano;

}