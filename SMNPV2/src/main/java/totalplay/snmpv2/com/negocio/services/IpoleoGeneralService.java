package totalplay.snmpv2.com.negocio.services;

import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;

public interface IpoleoGeneralService {
	<T extends GenericPoleosDto> CompletableFuture<String> getPoleo(Integer idMetrica, configuracionDto configuracion, Integer IdOlt, String idMonitorPoleo, ConfiguracionMetricaEntity confMetrica, Class<T> entidad) throws Exception;
	<T extends GenericPoleosDto> CompletableFuture<String> getPoleoIndividual(Integer idMetrica, String comando,  
			Integer idOlt, String referenceOnt, String tecnologia, String idMonitorPoleo, Integer region, int configuracion, ConfiguracionMetricaEntity confMetrica,  Class<T> entidad) throws Exception;
}
