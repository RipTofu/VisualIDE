package back_ptitulo.DTO;

public class UsuarioDTO {
    private Integer usuarioID;
    private String email;
    private String nombre;
    private int almacenamientodisponible;
    private int archivosguardados;

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getAlmacenamientodisponible() {
        return almacenamientodisponible;
    }

    public int getArchivosguardados() {
        return archivosguardados;
    }

    public String getEmail() {
        return email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setAlmacenamientodisponible(int almacenamientodisponible) {
        this.almacenamientodisponible = almacenamientodisponible;
    }

    public void setArchivosguardados(int archivosguardados) {
        this.archivosguardados = archivosguardados;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
