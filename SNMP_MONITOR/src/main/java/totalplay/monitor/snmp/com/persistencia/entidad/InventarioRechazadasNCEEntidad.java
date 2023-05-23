package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "inv_rechazados_act_nce")
@Data
@NoArgsConstructor

public class InventarioRechazadasNCEEntidad {
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