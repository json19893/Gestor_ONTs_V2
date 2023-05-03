package totalplay.snmpv2.com.negocio.services;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IasyncMethodsService {
   
	CompletableFuture<GenericResponseDto> getFaltantes(List<CatOltsEntity> olts );
	CompletableFuture<GenericResponseDto> getMetrica(List<CatOltsEntity> olts, int metrica, boolean manual );
	CompletableFuture<GenericResponseDto> getFaltatesInv(List<CatOltsEntity> olts );
	CompletableFuture<GenericResponseDto> saveEmpresariales(List onts, boolean manual );
	CompletableFuture<GenericResponseDto> deletePdm(List<InventarioOntsPdmEntity> onts );
	CompletableFuture<GenericResponseDto> joinUpdateStatus(List<CatOltsEntity> olts);
	CompletableFuture<GenericResponseDto> getFaltantesMetricas(List<CatOltsEntity> olts, String tabla, String joinField, int tipo, String idEjecucion, int idMetrica);
	CompletableFuture<GenericResponseDto> saveInventario(List<InventarioOntsEntity> onts );
	CompletableFuture<GenericResponseDto> putConfiguracion(List<CatOltsEntity> olts) throws Exception;
	CompletableFuture<GenericResponseDto> saveOntsNCE(List<BmsGestorOraVerticaEntity> onts) throws Exception;
	CompletableFuture<GenericResponseDto> deleteInventario(List<InventarioOntsEntity> onts );
}
