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
	private Integer olt;
	
	
	public monitorPoleoNCEEntidad(Date fecha_inicio, String descripcion, Integer estatus, Integer olt) {
		this.fecha_inicio = fecha_inicio;
		this.descripcion = descripcion;
		this.estatus = estatus;	
		this.olt = olt;
		
	}
}
