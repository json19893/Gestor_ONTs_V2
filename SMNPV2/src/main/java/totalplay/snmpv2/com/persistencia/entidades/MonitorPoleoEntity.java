package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "monitor_poleo")
@Data
@NoArgsConstructor
public class MonitorPoleoEntity {
	@Id
	private String id;
	private String fecha_inicio;
	private String fecha_fin;
	private String descripcion;
	private Integer estatus;
	
	public MonitorPoleoEntity(String fecha_inicio, String fecha_fin, String descripcion, Integer estatus) {
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}
	
}
