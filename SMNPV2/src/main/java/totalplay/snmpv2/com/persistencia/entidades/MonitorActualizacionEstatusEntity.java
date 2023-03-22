package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_monitor_cambio_estatus")
@Data
@NoArgsConstructor
public class MonitorActualizacionEstatusEntity {
	@Id
	private String id;
	private String fechaInicio;
	private String fechaFin;
	private String descripcion;
	public MonitorActualizacionEstatusEntity(String fechaInicio, String descripcion) {
		super();
		this.fechaInicio = fechaInicio;
		this.descripcion = descripcion;
	}
	
	
	
	
	 

}
