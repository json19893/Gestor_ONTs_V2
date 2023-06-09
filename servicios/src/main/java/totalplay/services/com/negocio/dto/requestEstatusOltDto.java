package totalplay.services.com.negocio.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class requestEstatusOltDto {
	private String ip;
	private String causa;
	private String descripcion;
	private String nombre;
	private Date fecha;
	private String status;
}
