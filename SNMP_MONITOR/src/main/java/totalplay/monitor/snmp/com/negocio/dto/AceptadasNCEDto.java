package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Data
public class AceptadasNCEDto extends inventarioOntsEntidad {
	private boolean inventario;
    
}
