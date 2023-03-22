package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class totalesOltsDto {
	
	public Integer totalOlt;
	public Integer arriba;
	public Integer abajo;
	public Integer cambios;
	
	public Integer getTotalOlt() {
		return totalOlt;
	}
	public void setTotalOlt(Integer totalOlt) {
		this.totalOlt = totalOlt;
	}
	public Integer getArriba() {
		return arriba;
	}
	public void setArriba(Integer arriba) {
		this.arriba = arriba;
	}
	public Integer getAbajo() {
		return abajo;
	}
	public void setAbajo(Integer abajo) {
		this.abajo = abajo;
	}
	public Integer getCambios() {
		return cambios;
	}
	public void setCambios(Integer cambios) {
		this.cambios = cambios;
	}
	
	

}
