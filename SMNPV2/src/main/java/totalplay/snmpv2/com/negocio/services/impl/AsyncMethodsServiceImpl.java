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
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.FaltantesDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.persistencia.repositorio.IauxiliarJoinEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioAuxTransRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsPdmRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsViejoGestorRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioPuertosRepository;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.OntsViejoGestorEntity;

import org.springframework.scheduling.annotation.Async;

import totalplay.snmpv2.com.persistencia.entidades.AuxiliarJoinEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsViejoGestorEntity;
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
	@Autowired
	IcatOltsRepository catOlts;
	@Autowired
	IinventarioOntsNCERepository inventarioNCE;
	@Autowired
	IinventarioOntsViejoGestorRepository inventarioViejo;
	@Autowired
	IinventarioOntsTempNCERepository tempNCE; 
	
	
	Utils util=new Utils();
	
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
				log.error("::::::::error olt "+ olt.getId_olt(),e );
			}
		}
		
		return null;
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> getMetrica(List<CatOltsEntity> olts, int metrica, boolean manual, boolean nce){
		for(CatOltsEntity olt:olts) {
			try {
					List resultado=null;
					LocalDateTime now = LocalDateTime.now();
					
					switch(metrica) {
						case 1:
							if(nce) {
								tempNCE.getEstatusNCE(olt.getId_region(),  olt.getId_olt());
								//tempNCE.saveAll(resultado);
								
							}else {
								resultado =inventarioAux.getEstatus(olt.getId_region(),  olt.getId_olt());
								auxiliarTrans.insert(resultado);
							}
						break;
						case 2:
							if(manual) 
								resultado = inventarioAuxManual.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							else if(nce)
								tempNCE.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							else	
								resultado = inventarioAux.getDescripcionAlarma(olt.getId_region(),  olt.getId_olt());
							if(!nce)
								auxiliarTrans.insert(resultado);
						break;
						case 4:
							if(manual) 
								resultado = inventarioAuxManual.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							else if(nce)
								tempNCE.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							else
								resultado = inventarioAux.getLastDownTime(olt.getId_region(),  olt.getId_olt());
							
							if(!nce)
								auxiliarTrans.insert(resultado);
						break;
						case 14:
							if(manual) 
								resultado = inventarioAuxManual.getAlias(olt.getId_region(),  olt.getId_olt());
							else if(nce)
								tempNCE.getAlias(olt.getId_region(),  olt.getId_olt());
							else
								resultado = inventarioAux.getAlias(olt.getId_region(),  olt.getId_olt());
							
							if(!nce)
								auxiliarTrans.insert(resultado);
						break;
						case 16:
							if(manual) 
								resultado = inventarioAuxManual.getFrameSlotPort(olt.getId_region(),  olt.getId_olt());
							else if(nce)
								tempNCE.getFrameSlotPort(olt.getId_region(),  olt.getId_olt());
							else								
								resultado = inventarioAux.getFrameSlotPort(olt.getId_region(),  olt.getId_olt());
							if(!nce)
								auxiliarTrans.insert(resultado);
						break;
					}
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.error("::::::::error olt "+ olt.getId_olt() +"  :::::::::::::::::::" , e );
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
				log.error("::::::::error olt "+ olt.getId_olt(),e );
			}
		}
		
		return null;
		
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> saveEmpresariales(List onts, boolean manual ){
		
		try {
			
				LocalDateTime now = LocalDateTime.now(); 
				if(manual)
					inventarioAuxManual.saveAll(onts);
				else
					inventarioAux.saveAll(onts);
				
				int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
				log.info("::::::::    onts empresariales guardas   :::::::::::::::  "+ seconds);
				
			
			
			
		} catch (Exception e) {
			log.error("::::::::error onts empresariales "+ e );
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
			log.error("::::::::error onts empresariales ", e );
		}
		
		
		return null;
		
	} 
	
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> deleteInventario(List<InventarioOntsEntity> onts ){
		
		try {
			
				System.out.println("Entrò  deleteAll");
			
				LocalDateTime now = LocalDateTime.now(); 
				
				inventario.deleteAll(onts);
				
				int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
				log.info("::::::::    onts eliminadas  :::::::::::::::  "+ seconds);
				
			
			
			
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
				log.error("::::::::error olt "+ olt.getId_olt(),e );
			}
		}
		return null;
		
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> completarEstatus(List<CatOltsEntity> olts){
		
		for(CatOltsEntity olt:olts) {
			try {
				
					LocalDateTime now = LocalDateTime.now(); 
					
					List<AuxiliarJoinEstatusEntity> resp = inventario.completarInventario(olt.getId_olt());
					auxiliarJoinEstatus.insert(resp);				
					
					int seconds = (int) ChronoUnit.SECONDS.between(now, LocalDateTime.now());
					log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
				
			} catch (Exception e) {
				log.error("::::::::error olt "+ olt.getId_olt(),e );
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
							respPrueba = inventario.getFaltantesMetricas(olt.getId_region(),  olt.getId_olt(), tabla, joinField, idEjecucion, idMetrica);
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
							respPrueba = inventario.getFaltantesMetricas(olt.getId_region(),  olt.getId_olt(), tabla, joinField, idEjecucion, idMetrica);
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
					//log.info("::::::::olt "+ olt.getId_olt() +"   :::::::::::::::  "+ seconds);
					
				
				
				
			} catch (Exception e) {
				log.error("::::::::error olt "+ olt.getId_olt(),e );
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
			log.error("error", e);
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
			log.error("error", e);
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
			log.error("::::::::error onts empresariales ", e );
		}
		
		
		return null;
		
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> putConfiguracion(List<CatOltsEntity> olts) throws Exception {
		
		Integer configu = 0;
		String tecnologia = "";
		
		for(CatOltsEntity olt: olts) {
			try {
				boolean pin=util.vaidaPin(olt.getIp());				
				Integer estatus = 0;
				boolean proceso = false;
				String descripcion = "---";
				
				if (pin) {
					proceso = util.validaConfiguracion(comando1 + olt.getIp() + SPACE + OID_SYSTNAME, 1);
					if (proceso) {
						configu = 1;
						tecnologia = "HUAWEI";
						estatus = 1;
					} else {
						proceso = util.validaConfiguracion(comando2 + olt.getIp() + SPACE + OID_SYSTNAME, 3);
						if (proceso) {
							configu = 3;
							tecnologia = "ZTE";
							estatus = 1;
						} else {
							proceso = util.validaConfiguracion(comando4 + olt.getIp() + SPACE + OID_SYSTNAME, 4);
							if (proceso) {
								configu = 4;
								tecnologia = "ZTE";
								estatus = 1;
							} else {
								proceso = util.validaConfiguracion(comando5 + olt.getIp() + SPACE + OID_SYSTNAME, 5);
								if (proceso) {
									configu = 5;
									tecnologia = "FIBER HOME";
									estatus = 1;

								} else {
									proceso = util.validaConfiguracion(comando6 + olt.getIp() + SPACE + OID_RUN_STATUS_HUAWEI,
											6);
									if (proceso) {
										configu = 6;
										tecnologia = "HUAWEI";
										estatus = 1;
									} else {
										proceso = util.validaConfiguracion(
												comando6 + olt.getIp() + SPACE + OID_RUN_STATUS_HUAWEI, 7);
										if (proceso) {
											configu = 7;
											tecnologia = "HUAWEI";
											estatus = 0;
											descripcion = "No reconoce los mibs de las metricas";
										} else {
											configu = 0;
											tecnologia = olt.getTecnologia();
											estatus = 0;
											descripcion = "No reconoce las credenciales de acceso";
										}
									}

								}
							}
						}
					}
				
					
					olt.setDescripcion( configu != olt.getId_configuracion() ? "Cambio configuracion, antes:" + olt.getId_configuracion() : "Sin cambio " );
					olt.setId_configuracion(configu);
					olt.setTecnologia(tecnologia);
					olt.setPin(1);
					olt.setEstatus(estatus);
					//olt.setDescripcion(descripcion);
				}else {
					olt.setPin(0);
					olt.setEstatus(0);
					olt.setDescripcion("No es pineable");
				}
				catOlts.save(olt);
			} catch (Exception e) {
				log.error("error", e);
			}
			
			
		}
		
		return null;
		
	}
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> saveOntsNCE(List<BmsGestorOraVerticaEntity> onts) throws Exception {
		List<InventarioNCEEntity> res = new   ArrayList<InventarioNCEEntity>();
		
		for(BmsGestorOraVerticaEntity o: onts) {
			
			InventarioNCEEntity aux= new InventarioNCEEntity();
			
			aux.setCIA(o.getCIA());
			aux.setCreate_time(o.getCreate_time());
			aux.setEquipment_id(o.getEquipment_id());
			aux.setEtiqueta_ont(o.getEtiqueta_ont());
			aux.setFrame(o.getFrame());
			aux.setId_ont(o.getId_ont());
			aux.setIp_olt(o.getIp_olt());
			aux.setNombre_olt(o.getNombre_olt());
			aux.setNombre_ont(o.getNombre_ont());
			aux.setPort(o.getPort());
			aux.setSlot(o.getSlot());
			aux.setSn(o.getSn());
			aux.setStatus(o.getStatus());
			aux.setUser_vlan(o.getUser_vlan());
			res.add( aux );
			
		}
		
		inventarioNCE.insert(res);
		
		return null;
	}
	
	private void saveErrores(int idMetrica, List<GenericPoleosDto> onts) {
		
		
		 
		switch(idMetrica) {
		case 2:
			//poleoMetrica.saveAll(list);
			List data = getErrores(PoleosLastDownCauseEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 3:
			data = getErrores(PoleosLastUpTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 4:
			data = getErrores(PoleosLastDownTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 5:
			data = getErrores(PoleosUpBytesEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 6:
			data = getErrores(PoleosDownBytesEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 7:
			data = getErrores(PoleosTimeOutEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 8:
			data = getErrores(PoleosUpPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 9:
			data = getErrores(PoleosDownPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 10:
			data = getErrores(PoleosDropUpPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 11:
			data = getErrores(PoleosDropDownPacketsEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 12:
			data = getErrores(PoleosCpuEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 13:
			data = getErrores(PoleosLastUpTimeEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 14:
			data = getErrores(PoleosAliasEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 15:
			data = getErrores(PoleosProfNameEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
		case 16:
			data = getErrores(PoleosFrameSlotPortEntity.class, onts, idMetrica);
			genericMetrics.guardaInventario(idMetrica, data, false);
			break;
			
		
		}
		
	} 
	
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<GenericResponseDto> saveOntsViejoGestor(List<OntsViejoGestorEntity> onts) throws Exception{
		List<InventarioOntsViejoGestorEntity> res = new   ArrayList<InventarioOntsViejoGestorEntity>();
		try {
			for(OntsViejoGestorEntity o: onts) {
				InventarioOntsViejoGestorEntity inv = new InventarioOntsViejoGestorEntity();
				
				inv.setEstatus(Integer.parseInt( util.isBlankOrNull(o.getEstatus()) ? "0": o.getEstatus() ));
				inv.setFrame(Integer.parseInt(util.isBlankOrNull(o.getFrame()) ? "0": o.getEstatus() ));
				inv.setIp_olt(o.getIp_olt());
				inv.setLast_down_cause(o.getLast_down_cause());
				inv.setLast_down_time(o.getLast_down_time().equals("inalcanzable")? "":o.getLast_down_time());
				inv.setNumero_serie(o.getNumero_serie());
				inv.setPort(Integer.parseInt(util.isBlankOrNull(o.getPort()) ? "0": o.getEstatus() ));
				inv.setSlot(Integer.parseInt(util.isBlankOrNull(o.getSlot() ) ? "0": o.getEstatus() ));		
				
				res.add(inv);
			}
			
			inventarioViejo.insert(res);
		}catch (Exception e) {
			log.info(e.toString());
		}
		return null;
	}
	
}
