package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;



@Document(collection = "cat_configuracion")
@Data
@NoArgsConstructor
public class catConfiguracionEntidad {
	@Id
	private String _id;
	private Integer id_configuracion;
	private String usuario;
	private String password;
	private String frase;
	private String nivel;
	private String version;
	private String prot_privado;
	private String prot_encriptado;
	private Integer estatus;
	private String fecha;
	private Integer descubrimientos;
	private Integer ejecucion;
	private String inicioBloqueo;
	private String finBloqueo;
	
	
  
	

}
