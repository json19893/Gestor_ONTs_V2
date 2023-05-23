package totalplay.services.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(collection = "tb_inventario_onts_pdm")
@Data
@NoArgsConstructor
public class inventarioOntsTempEntidad {
	
	@Id
	private String _id;
	private String numero_serie;
	private String oid;
	private Date fecha_descubrimiento;
	private Integer id_olt;
	private Integer estatus;
	private String id_ejecucion;
	private String alias;
	private Integer id_region;
	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String tipo;
	private String descripcionAlarma;
	private String tecnologia;
	private String lastDownTime;
	private Integer vip;
	private boolean sa;
	

}
