package totalplay.login.com.negocio.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class UsuariosRequestDto {
	@NonNull
	private String nombreUsuario;
	@NonNull
	private String nombreCompleto;
	@NonNull
	private String correo;
	@NonNull
	private String rol;

}
