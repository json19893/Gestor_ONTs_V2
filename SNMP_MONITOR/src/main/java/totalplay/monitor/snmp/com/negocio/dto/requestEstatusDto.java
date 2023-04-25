package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class requestEstatusDto {

	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String ip;
	private String estatus;
	private String serie;
	private String tipo;
	private String motivo;
	private String oid;




	public requestEstatusDto( String serie, String ip, Integer frame, Integer slot, Integer port,  String estatus) {
		this.frame = frame;
		this.slot = slot;
		this.port = port;
		this.ip = ip;
		this.estatus = estatus;
		this.serie = serie;
	}

	

}