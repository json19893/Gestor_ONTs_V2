package totalplay.monitor.snmp.com.negocio.dto;

import java.io.BufferedReader;


import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class DescubrimientoNCEUsuariosDto  {
	private Integer olt;
	private String id_ejecucion;
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DescubrimientoNCEUsuariosDto) {
			return ((DescubrimientoNCEUsuariosDto)obj).getOlt().equals(this.getOlt());
		}
		return false;
	}
	
}
