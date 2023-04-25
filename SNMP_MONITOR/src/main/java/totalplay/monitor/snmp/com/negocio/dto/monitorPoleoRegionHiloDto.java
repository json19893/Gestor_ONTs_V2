package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class monitorPoleoRegionHiloDto {
	
	private String _id;
	private String id_region;
	private String hilo;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoOltDto> poleo_olt;
	
	
}
