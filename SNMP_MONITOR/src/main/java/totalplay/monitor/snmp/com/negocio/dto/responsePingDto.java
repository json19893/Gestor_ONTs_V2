package totalplay.monitor.snmp.com.negocio.dto;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class responsePingDto {	
	private ArrayList<ipsDto> ips;

	public ArrayList<ipsDto> getIps() {
		return ips;
	}

	public void setIps(ArrayList<ipsDto> ips) {
		this.ips = ips;
	}
		
}
