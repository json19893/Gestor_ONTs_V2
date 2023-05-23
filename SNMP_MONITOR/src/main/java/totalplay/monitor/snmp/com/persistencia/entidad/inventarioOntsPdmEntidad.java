package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_inventario_onts_pdm")
@Data
@NoArgsConstructor

public class inventarioOntsPdmEntidad {
	@Id
	private String _id;
	private String numero_serie;
	private String oid;
	private String fecha_descubrimiento;
	private Integer id_olt;
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
	private Integer actualizacion;
	private String id_puerto;
	private boolean sa;
	
	
}
