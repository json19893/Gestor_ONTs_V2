package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "tbl_cat_olts_viejas")
public class catOltsInventarioEntidad {
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
}
