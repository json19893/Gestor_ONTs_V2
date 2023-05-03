package totalplay.snmpv2.com.negocio.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.CatOltsVerticaEntity;

public interface IconnciliacionService {
    CompletableFuture<GenericResponseDto> updateTables();
    CompletableFuture<GenericResponseDto> saveIps(List<CatOltsVerticaEntity> olts);
    
}
