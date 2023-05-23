package totalplay.services.com.persistencia.entidad;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tb_inventario_onts_resp")
@Data
@NoArgsConstructor
public class InventarioOntResp {
    @Id
    private String _id;
    private String numero_serie;
    private String oid;
    private String fecha_descubrimiento;
    private Integer id_olt;
    private Integer estatus;
    private String id_ejecucion;
    private String alias;
    private Integer id_region;
    private Integer frame;
    private Integer slot;
    private Integer port;
    private String uid;
    private String tipo;
    private String descripcionAlarma;
    private String tecnologia;
    private String lastDownTime;
    private Integer actualizacion;
    private Integer vip;
    private Integer sa;
}
