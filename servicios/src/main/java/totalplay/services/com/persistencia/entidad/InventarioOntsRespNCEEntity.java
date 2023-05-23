package totalplay.services.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "tb_inventario_onts_resp_nce")
@Data
@NoArgsConstructor
public class InventarioOntsRespNCEEntity  {
	@Id
	private String _id;
	private String oid;
	private String uid;
	private String valor;
	private Integer id_olt;
	private Integer id_metrica;
	private Date fecha_poleo;
	private Date fecha_modificacion;
	private Date fecha_descubrimiento;
	private Integer estatus;
	private String id_ejecucion;
	private Integer id_region;
	private Integer frame;
	private Integer slot;
	private Integer port;
	private String id_puerto;
	private String numero_serie;
	private String tecnologia;
	private String index;
	private String indexFSP;
	private String descripcion;
	private boolean error;
	private String alias;
	private String tipo; 
	private String lastDownTime;
	private String descripcionAlarma;
	private Integer actualizacion;
	private Integer vip;
	private String fecha_actualizacion;
	private boolean sa;
	
}
