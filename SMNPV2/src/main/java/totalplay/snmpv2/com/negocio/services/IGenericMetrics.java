package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;

public interface IGenericMetrics { 
    
	<T extends GenericPoleosDto> CompletableFuture<GenericResponseDto> poleo(configuracionDto configuracion, String idProceso, Integer metrica,Integer idOlt,Class<T> entidad, boolean saveErroneos, String referencia, boolean error,boolean manual, boolean nce, boolean oltNCE, String ruta) throws IOException, NoSuchFieldException, NoSuchMethodException;
	void guardaInventario(Integer idMetrica, List list, boolean nce, boolean oltNCE);
}
