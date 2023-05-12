package totalplay.services.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class requestCambioOlt {

	private String ipAnterior;
	private String ipNueva;

	public String getIpAnterior() {
		return ipAnterior;
	}

	public void setIpAnterior(String ipAnterior) {
		this.ipAnterior = ipAnterior;
	}
	
	public String getIpNueva() {
		return ipNueva;
	}

	public void setIpNueva(String ipNueva) {
		this.ipNueva = ipNueva;
	}

}
