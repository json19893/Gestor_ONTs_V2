package totalplay.monitor.snmp.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "monitor_poleo")
@Data
@NoArgsConstructor

public class monitorPoleoEntidad {
	@Id
	private String _id;
	private Date fecha_inicio;
	private Date fecha_fin;
	private String descripcion;
	private Integer estatus;
	
	

}
