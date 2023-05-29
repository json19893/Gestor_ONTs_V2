package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponseDto {
	private String sms;
	private Integer cod;
	 public GenericResponseDto(String sms,Integer cod) {
	        this.sms = sms;
	        this.cod = cod;
	    }
}
