package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

public class respuestaCambioEstatusDto {
	
	private boolean success;
	private String message;
	private List<inventarioOntsEntidad> estatusONTs;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<inventarioOntsEntidad> getEstatusONTs() {
		return estatusONTs;
	}

	public void setEstatusONTs(List<inventarioOntsEntidad> estatusONTs) {
		this.estatusONTs = estatusONTs;
	}

}
