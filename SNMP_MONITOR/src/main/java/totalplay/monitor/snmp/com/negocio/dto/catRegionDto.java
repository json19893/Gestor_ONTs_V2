package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class catRegionDto {
	private String _id;
	private Integer id_region;
	private String nombre_region;
	private Integer estatus;

}
