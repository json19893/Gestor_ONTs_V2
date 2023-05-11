package totalplay.snmpv2.com.persistencia.entidades;

import java.util.List;

import javax.persistence.Column;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "tb_inventario_onts_viejo_gestor")
public class InventarioOntsViejoGestorEntity {
	@Id
	private String _id;
	private String numero_serie;
	private String ip_olt;
	private Integer frame;
	private Integer slot;
	private Integer port;
	private Integer estatus;
	private String last_down_cause;
	private String last_down_time;
}
