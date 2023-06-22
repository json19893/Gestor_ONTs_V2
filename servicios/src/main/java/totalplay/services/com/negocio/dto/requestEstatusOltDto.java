package totalplay.services.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
public class requestEstatusOltDto {
	@NonNull
	private String ip;
	@NonNull
	private String causa;
	@NonNull
	private String descripcion;
	@NonNull
	private String nombre;
	@NonNull
	private String fecha;
	@NonNull
	private String status;
}
