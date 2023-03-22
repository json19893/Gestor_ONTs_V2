package totalplay.services.com.negocio.dto;

import java.io.BufferedReader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ejecucionDto {
	
	private BufferedReader buffer;
	private Process proceso;
	private String error;

	public BufferedReader getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}

	public Process getProceso() {
		return proceso;
	}

	public void setProceso(Process proceso) {
		this.proceso = proceso;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}


	

}
