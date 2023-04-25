package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoOltEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoRegionEntidad;

@Data
@NoArgsConstructor

public class responseMonitoreo {
	private monitoreoOltEntidad olt;
	private monitoreoRegionEntidad entidad;

	
}
