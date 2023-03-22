package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;

public interface IGenericMetrics { 
    
    <T extends GenericPoleosDto> CompletableFuture<GenericResponseDto> poleo(configuracionDto configuracion, String idProceso,Integer metrica,Integer idOlt,Class<T> entidad, boolean saveErroneos, String referencia) throws IOException;
}
