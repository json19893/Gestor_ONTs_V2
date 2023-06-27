package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DetalleActualizacionesOltsPojo {

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
