package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class responseMetricasDto {
	private String lastUpTime;
	private String upBytes;
	private String DownBytes;
	private String timeOut;
	private String upPackets;
	private String downPackets;
	private String dropUpPackets;
	private String dropDownPackets;
	private String cpu;
	private String memoria;
	private String profName;
	
}
