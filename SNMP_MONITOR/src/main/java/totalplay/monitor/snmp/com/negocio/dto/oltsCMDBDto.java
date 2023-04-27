package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
 public class oltsCMDBDto {
 	private String resource_name;
 	private String host_name;
 	private String ip_address;
 	private String zregion;
 	

 	
 	public oltsCMDBDto(String resource_name, String host_name, String ip_address, String zregion) {
 		super();
 		this.resource_name = resource_name;
 		this.host_name = host_name;
 		this.ip_address = ip_address;
 		this.zregion = zregion;
 	}
 
 
 
 
 		
 
 }
