package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;

@ToString
@Getter
@Setter
public class detalleActualizadasDto {
	
	Integer total;
	List<vwActualizacionEntidad> detalle;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<vwActualizacionEntidad> getDetalle() {
		return detalle;
	}
	public void setDetalle(List<vwActualizacionEntidad> detalle) {
		this.detalle = detalle;
	}
	
	

}