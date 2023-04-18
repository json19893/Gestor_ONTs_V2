package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CambioManualOntOltRequest implements Serializable {
    private Integer idOlt;
    private String numeroSerie;
    private Integer frame;
    private Integer slot;
    private Integer port;
}