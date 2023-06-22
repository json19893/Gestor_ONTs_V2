package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.dto.RequestPostMetrica;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;

public interface ICadenaService {
	
	void getCadenas(boolean all);
	CompletableFuture<String> runCommands(Integer idMetrica);
}
