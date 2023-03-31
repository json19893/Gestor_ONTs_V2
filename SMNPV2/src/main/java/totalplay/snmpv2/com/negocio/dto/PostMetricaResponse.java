package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import totalplay.snmpv2.com.persistencia.entidades.PoleosEstatusEntity;

import java.io.Serializable;

@Data
public class PostMetricaResponse extends GenericResponseDto implements Serializable {
    private PoleosEstatusEntity poleometrica;
}
