package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_monitoreo_ejecucion")
@Data
@NoArgsConstructor
public class MonitorEjecucionEntity {
	@Id
	private String id;
	private String descripcion;
	private Date fecha_inicio;
	private Date fecha_fin;
	private Integer estatus;
	
	 public MonitorEjecucionEntity(String descripcion,Date fecha_inicio, Date fecha_fin,Integer estatus) {
	        this.descripcion = descripcion;
	        this.fecha_inicio = fecha_inicio;
	        this.fecha_fin=fecha_fin;
	        this.estatus=estatus;
	        
	    }

}
