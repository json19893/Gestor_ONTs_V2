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
	private Integer bloque;
	private String id_ejecucion;
	
	public MonitorPoleoManualEntity(String fecha_inicio, String fecha_fin, String descripcion, Integer estatus, Integer bloque, String id_ejecucion) {
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.bloque =bloque;
		this.id_ejecucion = id_ejecucion;
	}
	
}
