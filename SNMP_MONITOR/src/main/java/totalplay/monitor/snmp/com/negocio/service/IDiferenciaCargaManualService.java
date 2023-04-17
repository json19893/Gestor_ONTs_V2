package totalplay.monitor.snmp.com.negocio.service;



import totalplay.monitor.snmp.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.monitor.snmp.com.negocio.service.impl.DiferenciaCargaManualServiceImpl;

import java.util.List;

public interface IDiferenciaCargaManualService {
    List<DiferenciaCargaManualServiceImpl.AuxOntsAdapter> consultarCatalogoOntsRepetidas(final OntsRepetidasPorOltPostRequest request);
}
