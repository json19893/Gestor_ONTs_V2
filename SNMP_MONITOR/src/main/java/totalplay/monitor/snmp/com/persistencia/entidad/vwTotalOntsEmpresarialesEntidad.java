package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "vw_total_onts_empresariales")
@Data
@NoArgsConstructor
public class vwTotalOntsEmpresarialesEntidad {
	@Id
	private String	_id;	
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String 	descricion;
	private Integer 	id_region;
	private Integer total_onts;
	private String tecnologia;
	


}
