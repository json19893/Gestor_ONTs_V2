package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoOltEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoRegionEntidad;

@Getter
@Setter
@ToString
public class responseMonitoreo {
	private monitoreoOltEntidad olt;
	private monitoreoRegionEntidad entidad;
	
	public monitoreoOltEntidad getOlt() {
		return olt;
	}
	public void setOlt(monitoreoOltEntidad olt) {
		this.olt = olt;
	}
	public monitoreoRegionEntidad getEntidad() {
		return entidad;
	}
	public void setEntidad(monitoreoRegionEntidad entidad) {
		this.entidad = entidad;
	}
	
}
