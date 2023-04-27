package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class responseFindDto {
	
	private boolean success;
	private String message;
	private responseRegionDto data;
	private Integer idRegion;
	
	
}
