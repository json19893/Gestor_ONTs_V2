package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class totalesTecnologiaDto {
	
	public Integer total;
	public Integer arriba;
	public Integer abajo;
	public String tecnologia;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
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
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}		

}
