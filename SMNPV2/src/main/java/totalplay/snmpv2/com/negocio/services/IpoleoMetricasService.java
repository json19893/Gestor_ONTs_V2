package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.dto.PostMetricaResponse;
import totalplay.snmpv2.com.negocio.dto.RequestPostMetrica;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;

public interface IpoleoMetricasService {
	CompletableFuture<String> executeProcess(List<CatOltsEntity> olts, String idMonitorPoleo, int idMetrica) throws Exception;
	CompletableFuture<String> getMetricaEmpresarialesByMetrica(List<OntsConfiguracionDto> ontsEmpresariales, String idPoleo, int idMetrica);
	List<OntsConfiguracionDto> getOntsFaltantes(int idMetrica, String idEjecucion, boolean resultado, boolean empresariales, String tabla, int tipo, List<CatOltsEntity> olts );
	void joinUpdateStatus(String idMonitorPoleo);
    PostMetricaResponse getPoleoOntMetrica(RequestPostMetrica request) throws IOException,InterruptedException;
}
