package totalplay.login.com.negocio.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto extends ResponseGenericoDto {
	
	private String usuario;
	private String nombreCompleto;
	private String rol;
	

}
