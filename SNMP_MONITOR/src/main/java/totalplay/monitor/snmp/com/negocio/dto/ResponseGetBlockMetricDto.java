package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.monitor.snmp.com.persistencia.entidad.ConfiguracionMetricaEntity;

import java.util.List;

@Getter
@Setter
@ToString
public class ResponseGetBlockMetricDto extends responseDto {
    List<ConfiguracionMetricaEntity> list;
}
