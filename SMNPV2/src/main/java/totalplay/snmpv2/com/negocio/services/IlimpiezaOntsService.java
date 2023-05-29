package totalplay.snmpv2.com.negocio.services;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;

import java.io.IOException;
import java.util.List;

public interface IlimpiezaOntsService {
   
	boolean getInventarioPuertos(MonitorEjecucionEntity monitor, List<CatOltsEntity> olts);
	boolean getInventarioaux(MonitorEjecucionEntity monitor);
	void updateDescripcion(MonitorEjecucionEntity monitor, String descripcion);
	void LimpiezaManual(List<CatOltsEntity> olts, MonitorEjecucionEntity monitor);
	void saveOnts(List<InventarioOntsEntity> inventario);
	void deleteInventarioPdm();
	void LimpiezaNCE(List<CatOltsEntity> olts);
	
}
