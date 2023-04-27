package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_diferencias")
@Data
@NoArgsConstructor

public class tblDiferenciasEntidad {
	
	private String _id;
	private Integer id_ont;
	private String numero_serie;
	private String oid;
	private String fecha_descubrimiento;
	private Integer id_olt;
	private String id_ejecucion;
	private Integer estatus;

	
}
