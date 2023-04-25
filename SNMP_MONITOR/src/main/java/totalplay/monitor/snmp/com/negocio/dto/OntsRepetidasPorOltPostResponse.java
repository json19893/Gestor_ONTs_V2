package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.ToString;
import totalplay.monitor.snmp.com.negocio.service.impl.DiferenciaCargaManualServiceImpl;


import java.io.Serializable;
import java.util.List;

@Data

public class OntsRepetidasPorOltPostResponse implements Serializable {
    //List<EnvoltorioOntRepetidasOltsDto> ont;
    List<DiferenciaCargaManualServiceImpl.AuxOntsAdapter> onts;
}
