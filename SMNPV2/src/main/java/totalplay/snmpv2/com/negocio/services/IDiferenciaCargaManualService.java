package totalplay.snmpv2.com.negocio.services;

import totalplay.snmpv2.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.snmpv2.com.negocio.services.impl.DiferenciaCargaManualServiceImpl;

import java.util.List;

public interface IDiferenciaCargaManualService {
    List<DiferenciaCargaManualServiceImpl.AuxOntsAdapter> consultarCatalogoOntsRepetidas(Integer idOlt);
}
