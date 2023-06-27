package totalplay.services.com.negocio.dto;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.services.com.persistencia.entidad.detalleActualizacionesOltEntidad;

@Data
@NoArgsConstructor
public class responseEstatusOltDto {
    private String sms;
	private Integer cod;
    private Integer totalActualizadas;
	private Integer totalRecibidas;
    private List<detalleActualizacionesOltEntidad> noActualizadas;

}
