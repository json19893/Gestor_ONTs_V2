package totalplay.snmpv2.com.negocio.services;

import totalplay.snmpv2.com.negocio.dto.CambioManualOntOltRequest;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;

public interface IUpdateTotalOntsService {
    GenericResponseDto updateTotalOntsFromOlts();

    InventarioOntsEntity CambioManualOntdeOlt(InventarioOntsEntity ont);
}
