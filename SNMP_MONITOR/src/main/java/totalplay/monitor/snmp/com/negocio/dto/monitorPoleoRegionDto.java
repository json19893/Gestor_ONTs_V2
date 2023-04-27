package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class monitorPoleoRegionDto {
	
	private String _id;
	private String id_poleo;
	private Integer id_region;
	private String nombre;
	private String fecha_inicio;
	private String fecha_fin;
	private Integer estatus;
	private List<monitorPoleoRegionHiloDto> poleo_region_hilo;
	
	
	
	
}
