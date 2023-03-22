package totalplay.snmpv2.com.negocio.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;

public interface IpoleoMetricasService {   
	CompletableFuture<String> executeProcess(List<CatOltsEntity> olts, String idMonitorPoleo, int idMetrica) throws Exception;  
	CompletableFuture<String> getMetricaEmpresarialesByMetrica(List<OntsConfiguracionDto> ontsEmpresariales, String idPoleo, int idMetrica);
	List<OntsConfiguracionDto> getOntsFaltantes(int idMetrica, String idEjecucion, boolean resultado, boolean empresariales, String tabla, int tipo );
	void joinUpdateStatus(String idMonitorPoleo);
}
