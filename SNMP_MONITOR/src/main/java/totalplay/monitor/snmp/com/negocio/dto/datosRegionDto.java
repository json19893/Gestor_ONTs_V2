package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class datosRegionDto {

	public Integer idRegion = new Integer(0);
	public String region = "";
	public Integer totalOlt  = new Integer(0);
	public Integer totalRegion  = new Integer(0);
	public Integer totalOnt  = new Integer(0);
	public Integer arriba  = new Integer(0);
	public Integer abajo  = new Integer(0);
	public Integer cambios  = new Integer(0);
	public Integer estatus  = new Integer(0);
	public Integer sinInformacion  = new Integer(0);


	

}
