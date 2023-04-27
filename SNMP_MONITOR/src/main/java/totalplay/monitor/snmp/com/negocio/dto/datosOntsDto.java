package totalplay.monitor.snmp.com.negocio.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Data
@NoArgsConstructor
public class datosOntsDto {
	private String _id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String descricion;
	private String tecnologia;
	private String id_region;
	private String id_configuracion;
	private Integer estatus;
	public List<inventarioOntsEntidad> onts;
	public Integer TotalHist;

	

}
