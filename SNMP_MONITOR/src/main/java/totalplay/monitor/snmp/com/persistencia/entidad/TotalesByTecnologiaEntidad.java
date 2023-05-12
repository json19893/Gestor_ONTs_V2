package totalplay.monitor.snmp.com.persistencia.entidad;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "GetTotalesOntsByTecnoliga_tmp")
//@Document(collection = "GetTotalesOntsByTecnologia_tmp")
@Data
public class TotalesByTecnologiaEntidad {
    @Id
    private String id;
    private LocalDateTime dateTime;
    private String tipo;
    private String descripcionCorta;
    private String descripcionLarga;
    private List<datosRegionDto> resumenStatusOnts;
}
