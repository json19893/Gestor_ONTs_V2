package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class listInventarioOntsDto {
	
	private String _id;
	private String numero_serie;
	private String oid;
	private String fecha_descubrimiento;
	private String id_olt;
	private Integer estatus;
	private String id_ejecucion;
	private String alias;
	private Integer id_region;
	private Integer slot;
	private Integer frame;
	private Integer port;
	private String tipo;
	private String uid;
	private String descripcionAlarma;
	private boolean isSelected;
	

	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof listInventarioOntsDto) {
			return ((listInventarioOntsDto) obj).getNumero_serie().equals(this.getNumero_serie());
		}
		return false;
	}
	
}
