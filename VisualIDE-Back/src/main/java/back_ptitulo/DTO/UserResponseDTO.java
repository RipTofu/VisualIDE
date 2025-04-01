package back_ptitulo.DTO;

import lombok.Data;

@Data
public class UserResponseDTO {
    private String nombre;
    private String email;
    private String token;
}
