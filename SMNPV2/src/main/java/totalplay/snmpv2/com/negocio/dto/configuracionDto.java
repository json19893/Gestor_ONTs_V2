package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class configuracionDto extends GenericResponseDto {
	private String version;
	private String userName;
	private String password;
	private String level;
	private String protEn;
	private String phrase;
	private String protPriv;
	private String ip;
	private String comando;
	private Integer idRegion;
	private Integer idOlt;
	private String tecnologia;
	private Integer idConfiguracion;
	private String nombreOlt;
}
