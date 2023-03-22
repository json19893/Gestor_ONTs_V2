package totalplay.login.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_roles")
@Data
@NoArgsConstructor
public class RolesEntity {
	@Id
	private String id;
	private String rol;
	private Integer estatus;
	
	

}
