package totalplay.services.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.services.com.persistencia.entidad.detalleActualizacionesEntidad;

@Getter
@Setter
@ToString
public class respuestaStatusDto {
	private String sms;
	private Integer cod;
	private Integer totalActualizadas;
	private Integer totalRecibidas;
	private List<detalleActualizacionesEntidad>  noActualizadas;
	public String getSms() {
		return sms;
	}
	public void setSms(String sms) {
		this.sms = sms;
	}
	public Integer getCod() {
		return cod;
	}
	public void setCod(Integer cod) {
		this.cod = cod;
	}
	public Integer getTotalActualizadas() {
		return totalActualizadas;
	}
	public void setTotalActualizadas(Integer totalActualizadas) {
		this.totalActualizadas = totalActualizadas;
	}
	public Integer getTotalRecibidas() {
		return totalRecibidas;
	}
	public void setTotalRecibidas(Integer totalRecibidas) {
		this.totalRecibidas = totalRecibidas;
	}
	public List<detalleActualizacionesEntidad> getNoActualizadas() {
		return noActualizadas;
	}
	public void setNoActualizadas(List<detalleActualizacionesEntidad> noActualizadas) {
		this.noActualizadas = noActualizadas;
	}
	
	
	
	

}