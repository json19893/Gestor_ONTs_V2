package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.persistencia.entidad.ConfiguracionMetricaEntity;

import java.util.List;

@Data
@NoArgsConstructor

public class ResponseGetBlockMetricDto extends responseDto {
    List<ConfiguracionMetricaEntity> list;
}
