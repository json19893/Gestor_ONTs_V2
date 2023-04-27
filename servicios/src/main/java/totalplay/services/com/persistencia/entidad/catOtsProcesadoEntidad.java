package totalplay.services.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import lombok.NoArgsConstructor;


@Document(collection = "tbl_procesada_configuracion")
@Data
@NoArgsConstructor
public class catOtsProcesadoEntidad {
	@Id
	private String id;
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String tecnologia;
	private Integer id_region;
	private String p;
	private String frase;
	private String protocolo;
	private String valor;
	

}
