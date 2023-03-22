package totalplay.login.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_usuarios_permitidos")
@Data
@NoArgsConstructor
public class usuariosEntity {
	@Id
	private String id;
	private String nombreUsuario;
	private String nombreCompleto;
	private String correo;
	private String rol;
	private Integer estatus;
	private Integer sesion;
	private String fechaRegistro;
	private String fechaConexion;
	
	

}
