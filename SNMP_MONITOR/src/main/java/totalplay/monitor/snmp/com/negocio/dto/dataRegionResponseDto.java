package totalplay.monitor.snmp.com.negocio.dto;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;

@Data
@NoArgsConstructor
public class dataRegionResponseDto {
	@Id
	private String	id;	
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String 	descripcion;
	private Integer 	id_region;
	private Integer total_onts;
	private String tecnologia;
	private Integer estatus;
	
	
		@Override
	public boolean equals(Object obj) {
		if(obj instanceof vwTotalOntsEntidad) {
			return ((vwTotalOntsEntidad) obj).getIp().equals(this.getIp());
		}
		return false;
	}
}
