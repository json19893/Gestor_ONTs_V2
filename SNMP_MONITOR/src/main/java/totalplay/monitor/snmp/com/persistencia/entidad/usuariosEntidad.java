package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_usuarios")
@Data
@NoArgsConstructor

public class usuariosEntidad {
	@Id
	private String id;
	private String nombre;
	private String usuario;
	private String password;
	
	

}
