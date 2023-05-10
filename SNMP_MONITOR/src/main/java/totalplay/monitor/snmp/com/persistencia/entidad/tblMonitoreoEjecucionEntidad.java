package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "getFecha")
@Data
@NoArgsConstructor

public class tblMonitoreoEjecucionEntidad {
	@Id
	private String id;
	private String descripcion;
	private LocalDateTime fecha_inicio;
	private LocalDateTime fecha_fin;
	private Integer estatus;

	

}
