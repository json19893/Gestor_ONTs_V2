package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.dto.RequestPostMetrica;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;

public interface IpoleoMetricasService {
	CompletableFuture<String> executeProcess(List<CatOltsEntity> olts, String idMonitorPoleo, int idMetrica) throws Exception;
	CompletableFuture<String> getMetricaEmpresarialesByMetrica(List<OntsConfiguracionDto> ontsEmpresariales, String idPoleo, int idMetrica, boolean oltsNCE);
	List<OntsConfiguracionDto> getOntsFaltantes(int idMetrica, String idEjecucion, boolean resultado, boolean empresariales, String tabla, int tipo, List<CatOltsEntity> olts, Integer idOlt);
	String joinUpdateStatus(String idMonitorPoleo);
	GenericResponseDto getPoleoOntMetrica(RequestPostMetrica request) throws Exception;
	CompletableFuture<String> getCadenaEmpresariales(List<OntsConfiguracionDto> ontsEmpresariales);  
	<T extends GenericPoleosDto> CompletableFuture<String> poleoEmpresarialesByShell(List<String> onts, Integer  idMetrica, String tecnologia);
	

}
