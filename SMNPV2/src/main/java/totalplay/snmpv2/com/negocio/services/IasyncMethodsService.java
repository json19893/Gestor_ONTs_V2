package totalplay.snmpv2.com.negocio.services;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IasyncMethodsService {
   
	CompletableFuture<GenericResponseDto> getFaltantes(List<CatOltsEntity> olts );
	CompletableFuture<GenericResponseDto> getMetrica(List<CatOltsEntity> olts, int metrica );
	CompletableFuture<GenericResponseDto> getFaltatesInv(List<CatOltsEntity> olts );
	CompletableFuture<GenericResponseDto> saveEmpresariales(List<InventarioOntsAuxEntity> onts );
	CompletableFuture<GenericResponseDto> deletePdm(List<InventarioOntsPdmEntity> onts );
	public CompletableFuture<GenericResponseDto> joinUpdateStatus(List<CatOltsEntity> olts);
}
