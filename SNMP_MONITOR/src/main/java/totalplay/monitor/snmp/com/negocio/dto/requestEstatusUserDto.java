package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class requestEstatusUserDto {

	private List<requestEstatusDto> lista;
	private String usuario;


}