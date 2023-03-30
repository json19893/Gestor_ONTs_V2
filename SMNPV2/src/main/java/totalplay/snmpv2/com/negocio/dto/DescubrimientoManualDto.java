package totalplay.snmpv2.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class DescubrimientoManualDto {
    private List<String> olts;
	private String usuario;
	private Integer bloque;
    
}
