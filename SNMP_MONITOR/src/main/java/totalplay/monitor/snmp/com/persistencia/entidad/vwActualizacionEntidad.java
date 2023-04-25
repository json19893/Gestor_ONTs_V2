package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "vw_actualizacion")
@Data
@NoArgsConstructor

public class vwActualizacionEntidad {
	//@Id
	private String _id;
	private String numero_serie;
	private String fecha_descubrimiento;
	private Integer estatus;
	private Integer id_region;
	private String tipo;
	private String actualizacion;
	


	
}
