package totalplay.snmpv2.com.negocio.dto;

import java.io.BufferedReader;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EjecucionDto extends GenericResponseDto {
	private BufferedReader buffer;
	private Process proceso;
	private String error;
	private String file;
	private String comando;
	private String oid;
	private boolean errorOlt;
	private boolean sinOid;
	private String ip;
	
}
