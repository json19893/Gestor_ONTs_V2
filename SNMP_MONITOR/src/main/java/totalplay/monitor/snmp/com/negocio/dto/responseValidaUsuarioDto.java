package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class responseValidaUsuarioDto {
	private boolean successs;
	private String mensaje;
	
	
	public boolean isSuccesss() {
		return successs;
	}
	public void setSuccesss(boolean successs) {
		this.successs = successs;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}
