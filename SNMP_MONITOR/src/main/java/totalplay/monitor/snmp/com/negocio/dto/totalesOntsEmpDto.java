package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Getter
@Setter
@ToString
public class totalesOntsEmpDto {
	
	private String tecnologia;
	private Integer total;
	private Integer arriba;
	private Integer abajo;
	private Integer sin_informacion;
	
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
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
	public Integer getSin_informacion() {
		return sin_informacion;
	}
	public void setSin_informacion(Integer sin_informacion) {
		this.sin_informacion = sin_informacion;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof totalesOntsEmpDto) {
			return ((totalesOntsEmpDto) obj).getTecnologia().equals(this.getTecnologia());
		}
		return false;
	}
}
