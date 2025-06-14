package totalplay.monitor.snmp.com.persistencia.entidad;

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
	private String fechaActualizacion;
	private String usuario;
	

}