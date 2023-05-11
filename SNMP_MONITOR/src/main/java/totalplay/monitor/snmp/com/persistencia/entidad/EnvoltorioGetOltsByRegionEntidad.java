package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioTopLevelRegionAuxiliarDto;

import javax.persistence.Id;
import java.time.LocalDateTime;

//Top level estructura:
@Document(collection = "GetOltsByRegion_tmp")
@Data
public class EnvoltorioGetOltsByRegionEntidad {
    @Id
    private String id;
    private LocalDateTime date;
    private EnvoltorioTopLevelRegionAuxiliarDto region1;
    private EnvoltorioTopLevelRegionAuxiliarDto region2;
    private EnvoltorioTopLevelRegionAuxiliarDto region3;
}
