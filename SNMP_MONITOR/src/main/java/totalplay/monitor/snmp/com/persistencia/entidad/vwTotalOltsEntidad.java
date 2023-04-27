package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "totalesRegion")
@Data
@NoArgsConstructor

public class vwTotalOltsEntidad {
	public Integer id_region;
	public Integer totalOlts;

	

}
