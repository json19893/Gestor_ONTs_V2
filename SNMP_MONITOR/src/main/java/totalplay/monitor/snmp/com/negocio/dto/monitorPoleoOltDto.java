package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class monitorPoleoOltDto {

	private String _id;
	private String id_region_hilo;
	private Integer id_olt;
	private String nombre_olt;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoOltMetricasDto> metricas;
	

		
}
