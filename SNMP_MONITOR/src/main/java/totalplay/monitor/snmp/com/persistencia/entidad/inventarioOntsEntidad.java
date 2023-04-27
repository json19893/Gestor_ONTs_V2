package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "tb_inventario_onts")
@Data
@NoArgsConstructor
public class inventarioOntsEntidad {
	@Id
	private String _id;
	private String numero_serie;
	private String oid;
	private Date fecha_descubrimiento;
	private Date fecha_modificacion;
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
	private String lastDownTime;
	private Integer actualizacion;
	private String id_puerto;
	private String tecnologia;
	private String index;
	private String indexFSP;
	private boolean error;
	
	

}