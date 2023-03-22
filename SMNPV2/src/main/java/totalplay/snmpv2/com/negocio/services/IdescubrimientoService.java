package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;

public interface IdescubrimientoService {
    CompletableFuture<GenericResponseDto> getDescubrimiento(List<CatOltsEntity> olts,String idProceso,boolean manual) throws IOException;
    
}
