package totalplay.services.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class respuestaDto {
	private String sms;
	private Integer cod;
	List<datosNumeroSerieDto> numeroSerie;
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
	public List<datosNumeroSerieDto> getNumeroSerie() {
		return numeroSerie;
	}
	public void setNumeroSerie(List<datosNumeroSerieDto> numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
	
	
	
	
	

}
