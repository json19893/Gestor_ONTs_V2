package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Data
@NoArgsConstructor

public class responseFindOntDto {
	
	private boolean success;
	private Integer page;
	private Integer idRegion;
	private String numeroSerie;
	private String message;
	private Integer idOlt;
	private List<inventarioOntsEntidad> listOnts;
	private totalesOltsDto totales;
	private String nombre;
	private String ip;
	
	
	
}
