package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class monitorPoleoOltMetricasDto {
	
	private String _id;
	private String id_poleo_olt;
	private String hilo;
	private Integer id_metrica;
	private String nombre_metrica; 
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	
}
