package totalplay.monitor.snmp.com.persistencia.entidad;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "tbl_carga_ont_sa")
@NoArgsConstructor
@Data
public class CargaOntSaEntity {

    @Id
    private String id;
    private String serie;

}
