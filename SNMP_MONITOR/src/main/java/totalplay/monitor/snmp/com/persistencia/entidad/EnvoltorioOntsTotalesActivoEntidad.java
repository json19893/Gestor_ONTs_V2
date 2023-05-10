package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Document(collection = "OntEstatusTotales_tmp")
@Data
public class EnvoltorioOntsTotalesActivoEntidad {
    @Id
    private String id;
    private LocalDateTime date;
    private EnvoltorioAuxiliarDto totalesOntsActivas;
    private EnvoltorioAuxiliarDto totalesOnstsActivasEmpresariales;
    private EnvoltorioAuxiliarDto totalesOntsActivasVips;

}
