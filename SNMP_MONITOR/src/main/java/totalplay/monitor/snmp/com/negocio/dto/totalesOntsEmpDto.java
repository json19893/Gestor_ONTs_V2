package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Data
@NoArgsConstructor

public class totalesOntsEmpDto {
	
	private String tecnologia;
	private Integer total;
	private Integer arriba;
	private Integer abajo;
	private Integer sin_informacion;
	
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof totalesOntsEmpDto) {
			return ((totalesOntsEmpDto) obj).getTecnologia().equals(this.getTecnologia());
		}
		return false;
	}
}
