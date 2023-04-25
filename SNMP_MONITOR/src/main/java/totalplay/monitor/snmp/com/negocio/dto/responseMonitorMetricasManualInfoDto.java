package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.MonitorPoleoManualEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;


@Data
@NoArgsConstructor

public class responseMonitorMetricasManualInfoDto extends MonitorPoleoManualEntity {
	private Integer [] terminados;
	private Integer [] faltantes;
	private Integer [] procesando;
	
	
	
}
