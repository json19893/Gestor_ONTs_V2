package totalplay.snmpv2.com.persistencia.entidades;

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
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	
	 public MonitorEjecucionEntity(String descripcion,String fecha_inicio, String fecha_fin,Integer estatus) {
	        this.descripcion = descripcion;
	        this.fecha_inicio = fecha_inicio;
	        this.fecha_fin=fecha_fin;
	        this.estatus=estatus;
	        
	    }

}
