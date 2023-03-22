package totalplay.snmpv2.com.negocio.services.impl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.persistencia.repositorio.IauxiliarEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioAuxTransRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsPdmRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioPuertosRepository;
import org.springframework.scheduling.annotation.Async;

import totalplay.snmpv2.com.persistencia.entidades.AuxiliarEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

@Slf4j
@Service
public class AsyncMethodsServiceImpl extends Constantes implements IasyncMethodsService {

	@Autowired
	IinventarioPuertosRepository inventarioPuertos;
	@Autowired
	IinventarioOntsTempRepository inventarioTmp;
	@Autowired
	IinventarioOntsAuxRepository inventarioAux;
	@Autowired
	IinventarioAuxTransRepository auxiliarTrans;	
	@Autowired
	IinventarioOntsRepository inventario;
	@Autowired
	IinventarioOntsPdmRepository inventarioPdm;
	@Autowired
	IauxiliarEstatusRepository auxiliarEstatus;
	
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> getFaltantes(List<CatOltsEntity> olts ){
		for(CatOltsEntity olt:olts) {
			try {
				
					LocalDateTime now = LocalDateTime.now(); 
					List<InventarioPuertosEntity> faltantes = inventarioTmp.findFaltantesPuertos(olt.getId_region(),  olt.getId_olt());
					
					if(faltantes.size()>0) {
						inventarioPuertos.insert(faltantes);
					}
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.info("::::::::error olt "+ olt.getId_olt() );
			}
		}
		
		return null;
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> getMetrica(List<CatOltsEntity> olts, int metrica ){
		for(CatOltsEntity olt:olts) {
			try {
					List resultado;
					LocalDateTime now = LocalDateTime.now();
					
					switch(metrica) {
						case 1:
							resultado =inventarioAux.getEstatus(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 2:
							resultado = inventarioAux.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 4:
							resultado = inventarioAux.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 14:
							resultado = inventarioAux.getAlias(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 16:
							resultado = inventarioAux.getFrameSlotPort(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
					}
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.info("::::::::error olt "+ olt.getId_olt() +"  :::::::::::::::::::" + e );
			}
		}
		
		return null;
	}
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> getFaltatesInv(List<CatOltsEntity> olts ){
		for(CatOltsEntity olt:olts) {
			try {
				
					LocalDateTime now = LocalDateTime.now(); 
					
					List<InventarioOntsAuxEntity> resp = inventario.getOntsfaltantes(olt.getId_region(),  olt.getId_olt());
					
					if(resp.size()>0) {
						inventarioAux.insert(resp);
					}
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.info("::::::::error olt "+ olt.getId_olt() );
			}
		}
		
		return null;
		
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> saveEmpresariales(List<InventarioOntsAuxEntity> onts ){
		
		try {
			
				LocalDateTime now = LocalDateTime.now(); 
				
				inventarioAux.saveAll(onts);
				
				int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
				log.info("::::::::    onts empresariales guardas   :::::::::::::::  "+ seconds);
				
			
			
			
		} catch (Exception e) {
			log.info("::::::::error onts empresariales "+ e );
		}
		
		
		return null;
		
	} 
	
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> deletePdm(List<InventarioOntsPdmEntity> onts ){
		
		try {
			
				LocalDateTime now = LocalDateTime.now(); 
				
				inventarioPdm.deleteAll(onts);
				
				int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
				log.info("::::::::    onts empresariales guardas   :::::::::::::::  "+ seconds);
				
			
			
			
		} catch (Exception e) {
			log.info("::::::::error onts empresariales "+ e );
		}
		
		
		return null;
		
	} 
	
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> joinUpdateStatus(List<CatOltsEntity> olts){
		
		for(CatOltsEntity olt:olts) {
			try {
				
					LocalDateTime now = LocalDateTime.now(); 
					
					List<AuxiliarEstatusEntity> resp = inventario.updateEstatus(olt.getId_region(),  olt.getId_olt());
					auxiliarEstatus.insert(resp);				
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.info("::::::::error olt "+ olt.getId_olt() );
			}
		}
		
		
		
		return null;
		
	} 
	
}
