package totalplay.snmpv2.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import totalplay.snmpv2.com.negocio.services.impl.DiferenciaCargaManualServiceImpl;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class EnvoltorioOntRepetidasOltsDto extends GenericPoleosDto {
    private String alias;
    private String tipo;
    private String lastDownTime;
    private String descripcionAlarma;
    private Integer actualizacion;
    private Integer vip;
    private String fecha_actualizacion;
    List<DiferenciaCargaManualServiceImpl.AuxTemplateOlt> oltList;
}
