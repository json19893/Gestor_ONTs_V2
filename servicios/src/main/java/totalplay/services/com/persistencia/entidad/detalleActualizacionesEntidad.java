package totalplay.services.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;



@Document(collection = "tb_detalle_actualizaciones")
@Data
@NoArgsConstructor
public class detalleActualizacionesEntidad {
	private String ip;
	private String numeroSerie;
	private String causa;
	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String descripcionAlarma;
	private Date fechaActualizacion;

}