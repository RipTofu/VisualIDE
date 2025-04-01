package back_ptitulo.dto;

import java.time.Instant;

public class ArchivoDTO {

    private Integer id;
    private String nombre;
    private String contenido;
    private Instant fecha;
    private Integer tamano;

    public ArchivoDTO(Integer id, String nombre, String contenido, Instant fecha, Integer tamano) {
        this.id = id;
        this.nombre = nombre;
        this.contenido = contenido;
        this.fecha = fecha;
        this.tamano = tamano;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Integer getTamano() {
        return tamano;
    }

    public void setTamano(Integer tamano) {
        this.tamano = tamano;
    }
}
