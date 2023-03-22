package totalplay.snmpv2.com.persistencia.entidades;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "cat_olts")
public class CatOltsEntity {
	@Id
	private String _id;
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
	private Integer onts_exito;
	private Integer onts_error;
	private List<CatConfiguracionEntity> configuracion;
	
}
