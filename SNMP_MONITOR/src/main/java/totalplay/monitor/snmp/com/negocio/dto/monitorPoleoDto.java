package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class monitorPoleoDto {
	private String _id;
	private String fecha_inicio;
	private String fecha_fin;
	private String descripcion;
	private Integer estatus;
	private List<monitorPoleoRegionDto> poleo_region;
	
	

	
}
