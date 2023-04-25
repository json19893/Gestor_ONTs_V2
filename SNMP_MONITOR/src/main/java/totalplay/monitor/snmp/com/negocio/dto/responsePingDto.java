package totalplay.monitor.snmp.com.negocio.dto;

import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class responsePingDto {	
	private ArrayList<ipsDto> ips;

	
}
