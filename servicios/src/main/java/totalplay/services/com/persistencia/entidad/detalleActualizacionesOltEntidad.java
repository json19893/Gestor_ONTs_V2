package totalplay.services.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "tb_detalle_actualizaciones_Olts")
public class detalleActualizacionesOltEntidad {
    @Id
    private String id;
    private String ip;
	private String causa;
	private String descripcion;
	private String nombre;
	private Date fechaRecibida;
	private Date fechaRegistro;
	private String status;
	private Integer correcta;//1-error,0-exito
    
}
