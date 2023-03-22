package totalplay.monitor.snmp.com.persistencia.entidad;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

@Document(collection = "tb_configuracion_metricas")
@NoArgsConstructor
@Data
public class ConfiguracionMetricaEntity {
    @Id
    private String id;
    private int id_metrica;
    private List<Integer> bloque;
    private List<Integer> id_configuracion;
    private String nombre;
    private Boolean activo;
    private String tiempo;
    private ObjectIdentified HUAWEI;
    private ObjectIdentified ZTE;
    private ObjectIdentified FH;
    private Boolean poleo_general;
}
