package totalplay.snmpv2.com.negocio.dto;

import java.io.BufferedReader;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CadenasMetricasDto extends GenericResponseDto {
	private String oid;
	private String split;
	private String type;
}
