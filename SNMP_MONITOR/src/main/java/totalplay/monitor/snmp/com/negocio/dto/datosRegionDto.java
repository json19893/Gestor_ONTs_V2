package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class datosRegionDto {

	public Integer idRegion;
	public String region;
	public Integer totalOlt;
	public Integer totalRegion;
	public Integer totalOnt;
	public Integer arriba;
	public Integer abajo;
	public Integer cambios;
	public Integer estatus;
	public Integer sinInformacion;


	

}
