package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Data
@NoArgsConstructor
public class InventarioRechazadoNCEDto extends catOltsEntidad {
	List<inventarioOntsEntidad> onts;

}
