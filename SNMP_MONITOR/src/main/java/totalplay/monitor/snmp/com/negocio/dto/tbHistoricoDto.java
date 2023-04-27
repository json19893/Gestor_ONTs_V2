package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class tbHistoricoDto {

	private String _id;
	private String numero_serie;
	private Integer id_olt;
	private String oid;
	private String fecha_descubrimiento;
	private Integer id_region;
	private String tipoCambio;
	private String alias;
	private String slot;
	private String frame;
	private String port;
	private String tipo;
	private String uid;
	private String descripcionAlarma;
	private String lastDownTime;
	
	
	
}
