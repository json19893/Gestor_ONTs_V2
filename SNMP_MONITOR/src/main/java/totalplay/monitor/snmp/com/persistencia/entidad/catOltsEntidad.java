package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "cat_olts")
@Data
@NoArgsConstructor
public class catOltsEntidad {
	@Id
	private String id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String descripcion;
	private String tecnologia;
	private Integer id_region;
	private Integer id_configuracion;
	private Integer estatus;
	private Integer pin;
	private boolean descubrio;
	private int total_onts;

	

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof catOltsEntidad) {
			return ((catOltsEntidad) obj).getIp().equals(this.getIp());
		}
		return false;
	}

}
