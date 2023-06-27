package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "tb_detalle_actualizaciones_Olts")
@NoArgsConstructor
@Data
public class DetalleActualizacionesOltsEntity {

    @Id
    private String id;
    private String ip;
    private String causa;
    private String descripcion;
    private String nombre;
    private String fechaRecibida;
    private String fechaRegistro;
    private String status;
    private String correcta;

}
