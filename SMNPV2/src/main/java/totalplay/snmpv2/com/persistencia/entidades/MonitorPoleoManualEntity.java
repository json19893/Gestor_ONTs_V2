package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "monitor_poleo_manual")
@Data
@NoArgsConstructor
public class MonitorPoleoManualEntity {
	@Id
	private String id;
	private String fecha_inicio;
	private String fecha_fin;
	private String descripcion;
	private Integer estatus;
	
	public MonitorPoleoManualEntity(String fecha_inicio, String fecha_fin, String descripcion, Integer estatus) {
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}
	
}
