package totalplay.snmpv2.com.negocio.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;

public interface IejecutaPoleoGeneralService {
	CompletableFuture<String> executeProcess(List<CatOltsEntity> olts,  Integer idRegion, String idMonitorPoleo, int idMetrica) throws Exception;
	
}