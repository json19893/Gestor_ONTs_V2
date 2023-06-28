package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class requestUpdateMetricasDto {
	private Integer idMetrica;
	private String oidFH;
	private String oidHuawei;
	private String oidZTE;	
	private String usario;
	
	
}
