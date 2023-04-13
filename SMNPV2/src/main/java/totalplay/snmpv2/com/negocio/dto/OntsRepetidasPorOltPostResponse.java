package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.ToString;
import totalplay.snmpv2.com.negocio.services.impl.DiferenciaCargaManualServiceImpl;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class OntsRepetidasPorOltPostResponse implements Serializable {
    //List<EnvoltorioOntRepetidasOltsDto> ont;
    List<DiferenciaCargaManualServiceImpl.AuxOntsAdapter> onts;
}
