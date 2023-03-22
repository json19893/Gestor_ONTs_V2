package totalplay.monitor.snmp.com.negocio.dto;
 
 
 public class oltsCMDBDto {
 	private String resource_name;
 	private String host_name;
 	private String ip_address;
 	private String zregion;
 	
 	public oltsCMDBDto() {
 		
 	}
 	
 	public oltsCMDBDto(String resource_name, String host_name, String ip_address, String zregion) {
 		super();
 		this.resource_name = resource_name;
 		this.host_name = host_name;
 		this.ip_address = ip_address;
 		this.zregion = zregion;
 	}
 
 
 
 	public String getResource_name() {
 		return resource_name;
 	}
 	public void setResource_name(String resource_name) {
 		this.resource_name = resource_name;
 	}
 	public String getHost_name() {
 		return host_name;
 	}
 	public void setHost_name(String host_name) {
 		this.host_name = host_name;
 	}
 	public String getIp_address() {
 		return ip_address;
 	}
 	public void setIp_address(String ip_address) {
 		this.ip_address = ip_address;
 	}
 	public String getZregion() {
 		return zregion;
 	}
 	public void setZregion(String zregion) {
 		this.zregion = zregion;
 	}
 		
 
 }
