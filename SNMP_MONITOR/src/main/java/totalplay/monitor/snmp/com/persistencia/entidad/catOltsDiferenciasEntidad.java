package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "cat_olts_diferencias")
@Data
@NoArgsConstructor

public class catOltsDiferenciasEntidad {
	@Id
	private String id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String descricion;
	private String tecnologia;
	private Integer id_region;
	private Integer id_configuracion;
	private Integer estatus;

	


}
