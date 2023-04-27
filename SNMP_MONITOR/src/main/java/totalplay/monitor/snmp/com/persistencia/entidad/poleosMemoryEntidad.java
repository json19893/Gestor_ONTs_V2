package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "tb_poleos_memory")
@Data
@NoArgsConstructor

public class poleosMemoryEntidad {
	@Id
	private String _id;
	private String oid;
	private String valor;
	private Integer id_olt;
	private Integer id_metrica;
	private String fecha_poleo;
	private Integer estatus;
	private String id_ejecucion;
	private Integer id_region;
	private Integer frame;
	private Integer slot;
	private Integer port;
	
	

	
}
