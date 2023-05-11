package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Document(collection = "getFecha")
@Data
@NoArgsConstructor

public class tblMonitoreoEjecucionEntidad {
	@Id
	private String id;
	private String descripcion;
	private Date fecha_inicio;
	private Date fecha_fin;
	private Integer estatus;

	

}
