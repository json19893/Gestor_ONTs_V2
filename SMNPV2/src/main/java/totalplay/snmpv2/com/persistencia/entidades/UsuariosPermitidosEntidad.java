package totalplay.snmpv2.com.persistencia.entidades;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.DescubrimientoNCEUsuariosDto;

@Document(collection = "tb_usuarios_permitidos")
@Data
@NoArgsConstructor
public class UsuariosPermitidosEntidad {
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
	private String ipConexion;
	private Integer intentos;
	private String[] ipConexionIntentos;
	private String ejecuciones;
	private List<DescubrimientoNCEUsuariosDto> descubrimientos ;

	

	
}
