package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Document(collection = "OntEstatusTotales2_tmp")
@Data
public class EnvoltorioOntsTotalesActivoEntidad {
    @Id
    private String id;
    private Integer idResumen;
    private LocalDateTime date;
    private EnvoltorioAuxiliarDto totalesOntsActivas;
    private EnvoltorioAuxiliarDto totalesOnstsActivasEmpresariales;
    private EnvoltorioAuxiliarDto totalesOntsActivasVips;
    private EnvoltorioAuxiliarDto totalesOntsActivasServiciosAdministrados;

}
