package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import totalplay.snmpv2.com.presentacion.MetricaController;

import java.io.Serializable;
import java.time.LocalTime;

@Data
public class PostMetricaResponse extends GenericResponseDto implements Serializable {
    private LocalTime horaPoleo;
    private MetricaController.MetricaPoleo metrica;
}
