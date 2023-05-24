package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_monitor_nce")
@Data
@NoArgsConstructor
public class monitorPoleoNCEEntidad {
	@Id
	private String id;
	private Date fecha_inicio;
	private Date fecha_fin;
	private String descripcion;
	private Integer estatus;
	private Integer bloque;
	private String id_ejecucion;
	
	public monitorPoleoNCEEntidad(Date fecha_inicio, Date fecha_fin, String descripcion, Integer estatus, Integer bloque, String id_ejecucion) {
		this.fecha_inicio = fecha_inicio;
		this.fecha_fin = fecha_fin;
		this.descripcion = descripcion;
		this.estatus = estatus;
		this.bloque =bloque;
		this.id_ejecucion = id_ejecucion;
	}
}
