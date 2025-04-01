package back_ptitulo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USUARIOS")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIOID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CENTROID", nullable = false)
    private Centroestudio centroid;

    @Size(max = 255)
    @NotNull
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Size(max = 255)
    @NotNull
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "CONTRASENA", nullable = false)
    private String contrasena;

    @NotNull
    @Column(name = "ALMACENAMIENTODISPONIBLE", nullable = false)
    private Integer almacenamientodisponible;

    @ColumnDefault("0")
    @Column(name = "ARCHIVOSGUARDADOS")
    private Integer archivosguardados;

    @OneToMany(mappedBy = "usuario")
    private Set<Archivo> archivos = new LinkedHashSet<>();

}