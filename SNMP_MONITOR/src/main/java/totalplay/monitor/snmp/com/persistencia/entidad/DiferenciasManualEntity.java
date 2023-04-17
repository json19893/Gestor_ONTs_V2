package totalplay.monitor.snmp.com.persistencia.entidad;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document(collection = "tb_diferencias_carga_manual")
@Data
@NoArgsConstructor
public class DiferenciasManualEntity {
    @Id
    private String _id;
    private String oid;
    private String uid;
    private String valor;
    private Integer id_olt;
    private Integer id_metrica;
    private Date fecha_poleo;
    private Date fecha_modificacion;
    private Integer estatus;
    private String id_ejecucion;
    private Integer id_region;
    private Integer frame;
    private Integer slot;
    private Integer port;
    private String id_puerto;
    private String numero_serie;
    private String tecnologia;
    private String index;
    private String indexFSP;
    private String descripcion;
    private boolean error;
}
