package totalplay.monitor.snmp.com.persistencia.entidad;



import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import totalplay.monitor.snmp.com.negocio.dto.diferenciasDto;

@Document(collection = "tb_historico_diferencias")
@Data
@NoArgsConstructor
public class tbHistoricoDiferenciasEntidad {

	@Id
	private String _id;
	private String numero_serie;
	private Integer id_olt;
	private String estatus;
	private String oid;
	private String fecha_descubrimiento;
	private String alias;
	private Integer id_region;
	private String slot;
	private String frame;
	private String port;
	private String tipo;
	private String descripcionAlarma;
	private String lastDownTime;
	private List<diferenciasDto> diferencias;
	
	
	

}
