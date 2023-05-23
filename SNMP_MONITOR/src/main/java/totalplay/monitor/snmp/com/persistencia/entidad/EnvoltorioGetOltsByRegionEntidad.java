package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioResumenOltsbyRegionEstatus;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioTopLevelRegionAuxiliarDto;

import javax.persistence.Id;
import java.time.LocalDateTime;

//Top level estructura:
@Document(collection = "GetOltsByRegion_tmp")
@Data
public class EnvoltorioGetOltsByRegionEntidad {
    @Id
    private String id;
    private int idRegion;
    private LocalDateTime fechaPoleo;
    private String nombreRegion;
    private String descripcion;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntTodoEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntEmpresarialesEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntVipsEstatus;
    private EnvoltorioResumenOltsbyRegionEstatus regionOntSAEstatus;
}
