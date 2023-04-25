package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "vw_total_onts")
@Data
@NoArgsConstructor
public class vwTotalOntsEntidad {
	@Id
	private String	id;	
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String 	descripcion;
	private Integer 	id_region;
	private Integer total_onts;
	private String tecnologia;
	private Integer estatus;
	
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof vwTotalOntsEntidad) {
			return ((vwTotalOntsEntidad) obj).getIp().equals(this.getIp());
		}
		return false;
	}
	

}
