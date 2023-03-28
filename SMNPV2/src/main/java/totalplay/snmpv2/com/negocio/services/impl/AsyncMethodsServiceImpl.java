package totalplay.snmpv2.com.negocio.services.impl;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.negocio.dto.FaltantesDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.persistencia.repositorio.IauxiliarJoinEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioAuxTransRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsPdmRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioPuertosRepository;
import org.springframework.scheduling.annotation.Async;

import totalplay.snmpv2.com.persistencia.entidades.AuxiliarJoinEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosAliasEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosCpuEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDownBytesEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDownPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDropDownPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDropUpPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosFrameSlotPortEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastDownCauseEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastDownTimeEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastUpTimeEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosProfNameEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosTimeOutEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosUpBytesEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosUpPacketsEntity;

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
	IauxiliarJoinEstatusRepository auxiliarJoinEstatus;
	@Autowired
	IfaltantesMetricasRepository faltantesMetricas;
	@Autowired
	IfaltantesMetricasManualRepository faltantesMetricasManual;
	@Autowired
	IfaltantesEstatusRepository faltantesEstatus;
	@Autowired
	IGenericMetrics genericMetrics;
	@Autowired
	IinventarioOntsAuxManualRepository inventarioAuxManual;
	
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
	public CompletableFuture<GenericResponseDto> getMetrica(List<CatOltsEntity> olts, int metrica, boolean manual){
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
							if(manual) 
								resultado = inventarioAuxManual.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							else 
								resultado = inventarioAux.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 4:
							if(manual) 
								resultado = inventarioAuxManual.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							else
								resultado = inventarioAux.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 14:
							if(manual) 
								resultado = inventarioAuxManual.getAlias(olt.getId_region(),  olt.getId_olt());
							else
								resultado = inventarioAux.getAlias(olt.getId_region(),  olt.getId_olt());
							auxiliarTrans.insert(resultado);
						break;
						case 16:
							if(manual) 
								resultado = inventarioAuxManual.getFrameSlotPort(olt.getId_region(),  olt.getId_olt());
							else
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
					
					List<AuxiliarJoinEstatusEntity> resp = inventario.updateEstatus(olt.getId_region(),  olt.getId_olt());
					auxiliarJoinEstatus.insert(resp);				
					
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
	public CompletableFuture<GenericResponseDto> getFaltantesMetricas(List<CatOltsEntity> olts, String tabla, String joinField, int tipo, String idEjecucion, int idMetrica){
		
		for(CatOltsEntity olt:olts) {
			try {
				
					LocalDateTime now = LocalDateTime.now(); 
					List resp;
					List<FaltantesDto> respPrueba;
					
					
					switch(tipo) {
						case 1:
							//List respPrueba;
							respPrueba = inventario.getFaltantesMetricas(olt.getId_region(),  olt.getId_olt(), tabla, joinField, idEjecucion);
							//faltantesMetricas.insert(respPrueba);
							
							//guardar en la tabla de poleos
							if(respPrueba != null && respPrueba.size() > 0) {
								if(respPrueba.get(0).getErrores()!= null)
									saveErrores(idMetrica, respPrueba.get(0).getErrores());
								else  if (respPrueba.get(0).getOnts()!= null ){
									List<FaltantesMetricasManualEntity> faltantes=  getFaltantes( FaltantesMetricasManualEntity.class, respPrueba.get(0).getOnts());
									faltantesMetricasManual.insert(faltantes);
								}
							}	
						break;
						case 2:
							
							//List respPrueba;
							respPrueba = inventario.getFaltantesMetricas(olt.getId_region(),  olt.getId_olt(), tabla, joinField, idEjecucion);
							//faltantesMetricas.insert(respPrueba);
							
							//guardar en la tabla de poleos
							if(respPrueba != null && respPrueba.size() > 0) {
								if(respPrueba.get(0).getErrores()!= null)
									saveErrores(idMetrica, respPrueba.get(0).getErrores());
								else  if (respPrueba.get(0).getOnts()!= null ){
									List<FaltantesMetricasEntity> faltantes=  getFaltantes( FaltantesMetricasEntity.class, respPrueba.get(0).getOnts());
									faltantesMetricas.insert(faltantes);
								}
							}
							
						break;
						case 3:
							resp = inventario.getFaltantesEstatus(olt.getId_region(),  olt.getId_olt(), tabla, joinField, idEjecucion);
							faltantesEstatus.insert(resp);
						break;
					}
									
						
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.info("::::::::error olt "+ olt.getId_olt() );
			}
		}
		
		
		
		return null;
		
	}
	
	private <T extends OntsConfiguracionDto> List<T> getFaltantes(Class<T> entidad, List<OntsConfiguracionDto> onts){

		List<T> response = new ArrayList<T>();
		
		try {
			for(OntsConfiguracionDto ont:onts) {
				T metrica = entidad.getConstructor().newInstance();
				metrica.setOnt(ont.getOnt());
				metrica.setConfiguracion(ont.getConfiguracion());
				metrica.setIp(ont.getIp());
				metrica.setTecnologia(ont.getTecnologia());
				metrica.setId_configuracion(ont.getId_configuracion());
				metrica.setError(ont.isError());
				metrica.setPoleable(ont.getPoleable());
				
				response.add(metrica);
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return response;
	}

	private <T extends GenericPoleosDto> List<T> getErrores(Class<T> entidad, List<GenericPoleosDto> onts, Integer idMetrica){

		List<T> response = new ArrayList<T>();
		
		try {
			for(GenericPoleosDto ont:onts) {
				T metrica = entidad.getConstructor().newInstance();
				metrica.setOid(ont.getOid());
				metrica.setError(true);
				metrica.setFecha_poleo(ont.getFecha_poleo());
				metrica.setValor(ont.getValor());
				metrica.setId_metrica(idMetrica);
				metrica.setId_ejecucion(ont.getId_ejecucion());
				metrica.setId_region(ont.getId_region());
				metrica.setTecnologia(ont.getTecnologia());
				metrica.setId_olt(ont.getId_olt());
				
				if(idMetrica==16)
					metrica.setIndexFSP(ont.getId_olt()+"-"+ont.getOid());
				else
					metrica.setIndex(ont.getId_olt()+"-"+ont.getOid());
				response.add(metrica);
			}			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return response;
	}
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> saveInventario(List<InventarioOntsEntity> onts ){
		
		try {
			
				LocalDateTime now = LocalDateTime.now(); 
				
				inventario.saveAll(onts);
				
				int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
				log.info("::::::::    onts empresariales guardas   :::::::::::::::  "+ seconds);			
			
			
		} catch (Exception e) {
			log.info("::::::::error onts empresariales "+ e );
		}
		
		
		return null;
		
	} 
	
	private void saveErrores(int idMetrica, List<GenericPoleosDto> onts) {
		
		
		 
		switch(idMetrica) {
		case 2:
			//poleoMetrica.saveAll(list);
			List data = getErrores(PoleosLastDownCauseEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 3:
			data = getErrores(PoleosLastUpTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 4:
			data = getErrores(PoleosLastDownTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 5:
			data = getErrores(PoleosUpBytesEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 6:
			data = getErrores(PoleosDownBytesEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 7:
			data = getErrores(PoleosTimeOutEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 8:
			data = getErrores(PoleosUpPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 9:
			data = getErrores(PoleosDownPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 10:
			data = getErrores(PoleosDropUpPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 11:
			data = getErrores(PoleosDropDownPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 12:
			data = getErrores(PoleosCpuEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 13:
			data = getErrores(PoleosLastUpTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 14:
			data = getErrores(PoleosAliasEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 15:
			data = getErrores(PoleosProfNameEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
		case 16:
			data = getErrores(PoleosFrameSlotPortEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data);
			break;
			
		
		}
		
	} 
	
}
