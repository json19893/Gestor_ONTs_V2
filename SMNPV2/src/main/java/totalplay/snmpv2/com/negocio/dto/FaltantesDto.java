package totalplay.snmpv2.com.negocio.dto;

import java.io.BufferedReader;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FaltantesDto {
	private List<OntsConfiguracionDto> onts;
	private List<GenericPoleosDto> errores;
	
	
}
