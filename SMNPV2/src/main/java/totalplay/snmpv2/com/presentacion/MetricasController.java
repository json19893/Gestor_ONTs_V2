package totalplay.snmpv2.com.presentacion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.DescubrimientoManualDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.services.IdescubrimientoService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorActualizacionEstatusEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IconfiguracionMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorActualizacionEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorEjecucionRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoOltMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IparametrosGeneralesRepository;
import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.ParametrosGeneralesEntity;
@Slf4j
@RestController

public class MetricasController extends Constantes {
	
	@Autowired
	ImonitorPoleoRepository monitorPoleo;
	@Autowired
	IcatOltsRepository catOlts;
	@Autowired
	IconfiguracionMetricaRepository confMetricas;
	@Autowired
	ImonitorPoleoMetricaRepository monitorMetrica;
	@Autowired
	IpoleoMetricasService poleoMetricas;
	@Autowired
	ImonitorPoleoOltMetricaRepository monitorOlt;
	@Autowired
	ImonitorActualizacionEstatusRepository monitorEstatus;
	@Autowired
	IparametrosGeneralesRepository parametrosGenerales;
	@Autowired
	IinventarioOntsRepository inventarioOnts;
	@Autowired
	ImonitorPoleoManualRepository monitorPoleoManual;
	
	Utils util= new Utils();
	
	
	
	@Scheduled(cron = "0 2 0 * * *", zone = "CST")
	private void cleanDatabase() {
		monitorMetrica.deleteAll();
		monitorOlt.deleteAll();
		
		ParametrosGeneralesEntity params = parametrosGenerales.getParametros(1);
		params.setBloque(1);
		parametrosGenerales.save(params);
	}
		
	//@Scheduled(fixedRate = 14400000)
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping(value = "/poleoMetricas", produces = MediaType.APPLICATION_JSON_VALUE)
	public String separatePoleo() throws Exception {
		String response = "ok";

		
		Integer estatusPoleo = FALLIDO;
		String idMonitorPoleo = "";
		String idMonitorMetrica = "";
		List<OntsConfiguracionDto> ontsEmpresariales;
		ParametrosGeneralesEntity params = null;
		Integer maxOntsEmpresariales;
		int activas = 0;
		MonitorPoleoMetricaEntity monitPoleoMetrica;
		boolean snmpBulkWalk=true;
		boolean reprocesoEmpresariales=false;
		
		ParametrosGeneralesEntity parametros =  parametrosGenerales.getParametros(1);
		if(parametros != null) {
			snmpBulkWalk = parametros.isSnmp_bulk_walk();
			reprocesoEmpresariales = parametros.isReproceso_empresariales(); 
		}
		
			
		
		try {
			
			
			//Se crea un nuevo registro para el monitor		
			idMonitorPoleo = monitorPoleo.save(new MonitorPoleoEntity(util.getDate(), null,INICIO_DESC+"POLEO" , INICIO)).getId();
			
		
			
			List<CatOltsEntity> olts = catOlts.findByEstatus(1);
			List<CompletableFuture<String>> regionSegmentOnts;
			List<CompletableFuture<String>> regionSegmentOntsEmpresariales;
			
			for(int j=2;j<=16;j++) {
				
				activas = confMetricas.getCountActive(j);
				
				if(activas>0){
					
					monitPoleoMetrica = monitorMetrica.save(new MonitorPoleoMetricaEntity(Integer.valueOf(j),util.getDate(),idMonitorPoleo));
					idMonitorMetrica = monitPoleoMetrica.getId(); 
					//monitPoleoMetrica = monitorMetrica.getMonitorMetrica(idMonitorMetrica);
					if(snmpBulkWalk) {
						regionSegmentOnts = new ArrayList<CompletableFuture<String>>();			
						
						Integer maxOnts = (olts.size() /110) + 1;
						
						for (int i = 0; i < olts.size(); i += maxOnts) {
							Integer limMax = i + maxOnts;
							if (limMax >= olts.size()) {
								limMax = olts.size();
							}
							List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
			
							CompletableFuture<String> executeProcess = poleoMetricas.executeProcess(listSegment, idMonitorPoleo, j);
							regionSegmentOnts.add(executeProcess);
						}
			
						CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();
					}
					
					//Obtener las empresariales no poeladas y mandarlas con otro servicio
					ontsEmpresariales =  poleoMetricas.getOntsFaltantes(j,idMonitorPoleo, true, reprocesoEmpresariales, "auxiliar", 2, null, null);
					
					monitPoleoMetrica.setOntsSnmp(ontsEmpresariales.size());
					monitPoleoMetrica.setFecha_corte(util.getDate());					
					
					log.info(ontsEmpresariales.size()+"---------------------------------------------");
					
					if(ontsEmpresariales != null) {
						log.info("::::::::::::::::::::::::::::::Se hace el cruce de faltantes para " + ontsEmpresariales.size() );
						regionSegmentOntsEmpresariales = new ArrayList<CompletableFuture<String>>();
						maxOntsEmpresariales = (ontsEmpresariales.size()/110) + 1;
						
						for (int i = 0; i < ontsEmpresariales.size(); i += maxOntsEmpresariales) {
							log.info("--------------------------------------------"+i+"------------------------------------");
							Integer limMax = i + maxOntsEmpresariales;
							if (limMax >= ontsEmpresariales.size()) {
								limMax = ontsEmpresariales.size();
							}
							
							List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(ontsEmpresariales.subList(i, limMax));
							
							
							CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,idMonitorPoleo, j, false);
							
							regionSegmentOntsEmpresariales.add(executeProcess);
						}
		
						CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
						
					}
					
					monitPoleoMetrica.setFecha_fin(util.getDate());
					monitorMetrica.save(monitPoleoMetrica);					
					
				}
			}
		} catch (Exception e) {
			log.info("error:" + e);
			response = "error:::" + e;
		}finally {
			MonitorPoleoEntity monitor = monitorPoleo.getMonitorPoleo(idMonitorPoleo);
			monitor.setFecha_fin(util.getDate());
			monitorPoleo.save(monitor);
		}

		return response;

	}

	
		@Scheduled(fixedRate =300000)
		@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
		@RequestMapping(value = "/updateStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public String UpdateStatus() throws Exception {

			Integer maxOlts = 50;
			Integer maxOnts = 1500;
			Integer maxOltsService = 50;
			String estatus =""; 
			String idMonitorPoleo = "";
			
			
			try {
				
				idMonitorPoleo = monitorEstatus.save(new MonitorActualizacionEstatusEntity(util.getDate(), INICIO_PUT_ESTATUS)).getId();
				List<CatOltsEntity> olts = catOlts.findByEstatus(1);
				ArrayList<CompletableFuture<String>> regionSegmentOnts = new ArrayList<CompletableFuture<String>>();			
				
				maxOnts = (olts.size() / 42) + 1;
				//se comenta para que no pole por snmpbulwalk
				/*for (int i = 0; i < olts.size(); i += maxOnts) {
					Integer limMax = i + maxOnts;
					if (limMax >= olts.size()) {
						limMax = olts.size();
					}
					List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(
							olts.subList(i, limMax));

					CompletableFuture<String> executeProcess = poleoMetricas.executeProcess(listSegment, idMonitorPoleo, 1);
					regionSegmentOnts.add(executeProcess);
				}

				CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();*/
				
				List<OntsConfiguracionDto> ontsEmpresariales =  poleoMetricas.getOntsFaltantes(1,idMonitorPoleo, true, true,"auxiliar_estatus", 3, null, null);
				
				ArrayList<CompletableFuture<String>> regionSegmentOntsEmpresariales = new ArrayList<CompletableFuture<String>>();
				Integer maxOntsEmpresariales = (ontsEmpresariales.size() /42) + 1;
				
				for (int i = 0; i < ontsEmpresariales.size(); i += maxOntsEmpresariales) {
					log.info("--------------------------------------------"+i+"------------------------------------");
					Integer limMax = i + maxOntsEmpresariales;
					if (limMax >= ontsEmpresariales.size()) {
						limMax = ontsEmpresariales.size();
					}
					
					List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(ontsEmpresariales.subList(i, limMax));
					
					
					CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,idMonitorPoleo, 1, false); 
					regionSegmentOntsEmpresariales.add(executeProcess);
				}

				CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
				estatus = "EXITOSO";
				
				//cruce de métricas
				//poleoMetricas.joinUpdateStatus(idMonitorPoleo);
				
			} catch (Exception e) {
				estatus = "ERRONEO";
			} finally {
				 MonitorActualizacionEstatusEntity monitEstatus = monitorEstatus.getMonitorEstatus(idMonitorPoleo);
				 monitEstatus.setFechaFin(util.getDate());
				 monitEstatus.setDescripcion(estatus);
				 monitorEstatus.save(monitEstatus);
			}
			

			return "Fin";

		}
		
		@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
		@PostMapping(value = "/poleoMetricasManual", produces = MediaType.APPLICATION_JSON_VALUE)
		public GenericResponseDto poleoMetricasManual(@RequestBody DescubrimientoManualDto datos) throws Exception {
			String response = "ok";
			Integer estatusPoleo = FALLIDO;
			String idMonitorPoleo = "";
			String idMonitorPoleoManual = "";
			String idMonitorMetrica = "";
			List<OntsConfiguracionDto> ontsEmpresariales;
			ParametrosGeneralesEntity params = null;
			Integer maxOntsEmpresariales;
			Integer bloque = datos.getBloque();
			
			int activas = 0;
			
			
			try {
				
				
				//Marcar el proceso iniciado
				params= parametrosGenerales.getParametros(1);
				if(params!=null && params.isPoleo_metrica()) {
					return new GenericResponseDto(EJECUCION_ERROR +"Ejecución en curso", 0);
				}else if( datos.getBloque() !=null && datos.getOlts() !=null && datos.getOlts().size()== 0  ) {
					return new GenericResponseDto(EJECUCION_ERROR +"Debe enviar información completa (boque y olts)", 0);
				}else {
					params.setBloque(bloque);
					params.setPoleo_metrica(true);
					parametrosGenerales.save(params);
				}
				
			} catch (Exception e) {
				log.info(e.toString());
				return new GenericResponseDto(EJECUCION_ERROR +"Error al obtener los datos de la ejecución", 0);
			}

			try {
				
				//monitorPoleo.findFirstByOrderByIdDesc();
				//Se crea un nuevo registro para el monitor
				//idMonitorPoleo = monitorPoleo.getLastFinishId().getId();
				idMonitorPoleo = monitorPoleo.findFirstByOrderByIdDesc().getId();
				idMonitorPoleoManual = monitorPoleoManual.save(new MonitorPoleoManualEntity(util.getDate(), null,INICIO_DESC+"POLEO" , INICIO, bloque, idMonitorPoleo )).getId();
				
				
				
				//List<CatOltsEntity> olts = catOlts.findByEstatus(1);
				List<CatOltsEntity> olts = catOlts.getOltsByIp(datos.getOlts());
				
				List<CompletableFuture<String>> regionSegmentOnts;
				List<CompletableFuture<String>> regionSegmentOntsEmpresariales;
				MonitorPoleoMetricaEntity monitPoleoMetrica;
				
				for(int j=2;j<=16;j++) {
					
					activas = confMetricas.getCountMetricasBloque(j, bloque);
					
					if(activas>0){
						
						idMonitorMetrica = monitorMetrica.save(new MonitorPoleoMetricaEntity(Integer.valueOf(j),util.getDate(),idMonitorPoleo)).getId();
						monitPoleoMetrica = monitorMetrica.getMonitorMetrica(idMonitorMetrica);
						
						regionSegmentOnts = new ArrayList<CompletableFuture<String>>();			
						
						Integer maxOnts = (olts.size() /50) + 1;
						
						for (int i = 0; i < olts.size(); i += maxOnts) {
							Integer limMax = i + maxOnts;
							if (limMax >= olts.size()) {
								limMax = olts.size();
							}
							List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
			
							CompletableFuture<String> executeProcess = poleoMetricas.executeProcess(listSegment, idMonitorPoleo, j);
							regionSegmentOnts.add(executeProcess);
						}
						
						CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();
						
						monitPoleoMetrica.setStop(LocalDateTime.now().toString());
						//Obtener las onts poeladas y mandarlas con otro servicio
						ontsEmpresariales =  poleoMetricas.getOntsFaltantes(j,idMonitorPoleo, true, false, "auxiliar_poleo_manual", 1, olts, null);						
						monitPoleoMetrica.setOntsSnmp(ontsEmpresariales.size());
						
						if(ontsEmpresariales != null) {
							regionSegmentOntsEmpresariales = new ArrayList<CompletableFuture<String>>();
							maxOntsEmpresariales = (ontsEmpresariales.size()/50) + 1;
							
							for (int i = 0; i < ontsEmpresariales.size(); i += maxOntsEmpresariales) {
								log.info("--------------------------------------------"+i+"------------------------------------");
								Integer limMax = i + maxOntsEmpresariales;
								if (limMax >= ontsEmpresariales.size()) {
									limMax = ontsEmpresariales.size();
								}
								
								List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(ontsEmpresariales.subList(i, limMax));
								
								
								CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,idMonitorPoleo, j, false);
								
								regionSegmentOntsEmpresariales.add(executeProcess);
							}
			
							CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
							
						}
						
//						monitPoleoMetrica.setFecha_fin(LocalDateTime.now().toString());
//						monitorMetrica.save(monitPoleoMetrica);
						
						
					}
				}
				
				estatusPoleo=EXITOSO;
				
			} catch (Exception e) {
				log.info("error:" + e);
				response = "error:::" + e;
			}finally {
				if(!idMonitorPoleoManual.equals("")) {
					MonitorPoleoManualEntity monitor = monitorPoleoManual.getMonitorPoleo(idMonitorPoleoManual);
					monitor.setFecha_fin(util.getDate());
					monitor.setEstatus(estatusPoleo);
					monitorPoleoManual.save(monitor);
				}
				
				if(params!=null) {
					params.setPoleo_metrica(false);
					parametrosGenerales.save(params);
				}
				
			}

			return new GenericResponseDto(FINAL_EXITO +" POLEO MANUAL", 1);

		}
		

}
