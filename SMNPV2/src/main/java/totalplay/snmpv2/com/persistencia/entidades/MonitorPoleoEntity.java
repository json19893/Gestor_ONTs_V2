package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

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
	private Date fecha_inicio;
	private Date fecha_fin;
	private String descripcion;
	private Integer estatus;
	
	public MonitorPoleoEntity(Date fecha_inicio, Date fecha_fin, String descripcion, Integer estatus) {
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.descripcion = descripcion;
		this.estatus = estatus;
	}
	
}
