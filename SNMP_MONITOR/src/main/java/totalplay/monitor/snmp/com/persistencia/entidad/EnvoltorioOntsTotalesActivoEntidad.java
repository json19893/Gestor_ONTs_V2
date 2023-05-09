package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Document(collection = "test_hist")
@Data
public class EnvoltorioOntsTotalesActivoEntidad {
    @Id
    private String id;
    private LocalDateTime date;
    private EnvoltorioAuxiliarDto totalesActivas;
    private EnvoltorioAuxiliarDto totalesActivasEmpresariales;
    private EnvoltorioAuxiliarDto totalesActivasVips;

}
