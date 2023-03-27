package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.MonitorPoleoManualEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;


@Getter
@Setter
@ToString
public class responseMonitorMetricasManualInfoDto extends MonitorPoleoManualEntity {
	private Integer [] terminados;
	private Integer [] faltantes;
	private Integer [] procesando;
	
	
	
}
