package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;


@Data
@NoArgsConstructor
public class detalleActualizadasDto {
	
	Integer total;
	List<vwActualizacionEntidad> detalle;
	
	
	

}