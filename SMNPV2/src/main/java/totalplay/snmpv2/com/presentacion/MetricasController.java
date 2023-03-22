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
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
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
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoOltMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IparametrosGeneralesRepository;
import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoEntity;
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
	@GetMapping(value = "/separatePoleo", produces = MediaType.APPLICATION_JSON_VALUE)
	public String separatePoleo() throws Exception {
		String response = "ok";

		
		Integer estatusPoleo = FALLIDO;
		String idMonitorPoleo = "";
		String idMonitorMetrica = "";
		List<OntsConfiguracionDto> ontsEmpresariales;
		ParametrosGeneralesEntity params = null;
		Integer maxOntsEmpresariales;
		int activas = 0;
		
			
		
		try {
			
			//monitorMetrica.deleteAll();
			//monitorOlt.deleteAll();
			//Se crea un nuevo registro para el monitor
			idMonitorPoleo = monitorPoleo.save(new MonitorPoleoEntity(LocalDateTime.now().toString(), null,INICIO_DESC+"POLEO" , INICIO)).getId();
			
			params =  parametrosGenerales.getParametros(1);
			
			List<CatOltsEntity> olts = catOlts.findByEstatus(1);
			List<CompletableFuture<String>> regionSegmentOnts;
			List<CompletableFuture<String>> regionSegmentOntsEmpresariales;
			
			for(int j=1;j<=16;j++) {
				
				activas = confMetricas.getCountMetricas(j, params.getBloque());
				
				if(activas>0){
					
					idMonitorMetrica = monitorMetrica.save(new MonitorPoleoMetricaEntity(Integer.valueOf(j),LocalDateTime.now().toString(),idMonitorPoleo)).getId();
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
					
					//Obtener las empresariales no poeladas y mandarlas con otro servicio
					ontsEmpresariales =  poleoMetricas.getOntsEmpreariales(j,idMonitorPoleo, true);
					
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
							
							
							CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,idMonitorPoleo, j);
							
							regionSegmentOntsEmpresariales.add(executeProcess);
						}
		
						CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
						
					}
					
					MonitorPoleoMetricaEntity monitPoleoMetrica = monitorMetrica.getMonitorMetrica(idMonitorMetrica);
					monitPoleoMetrica.setFecha_fin(LocalDateTime.now().toString());
					monitorMetrica.save(monitPoleoMetrica);
					
					
				}
			}
			
			
		} catch (Exception e) {
			log.info("error:" + e);
			response = "error:::" + e;
		}finally {
			MonitorPoleoEntity monitor = monitorPoleo.getMonitorPoleo(idMonitorPoleo);
			monitor.setFecha_fin(LocalDateTime.now().toString());
			monitorPoleo.save(monitor);
			
			params.setBloque(params.getBloque()+1);
			parametrosGenerales.save(params);
		}

		return response;

	}

	
		//@Scheduled(fixedRate = 14400000)
		@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
		@RequestMapping(value = "/updateStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public String UpdateStatus() throws Exception {

			Integer maxOlts = 50;
			Integer maxOnts = 1500;
			Integer maxOltsService = 50;
			String estatus =""; 
			String idMonitorPoleo = "";
			
			
			try {
				
				idMonitorPoleo = monitorEstatus.save(new MonitorActualizacionEstatusEntity(LocalDateTime.now().toString(), INICIO_PUT_ESTATUS)).getId();
									
				
				List<CatOltsEntity> olts = catOlts.findByEstatus(1);
				
				ArrayList<CompletableFuture<String>> regionSegmentOnts = new ArrayList<CompletableFuture<String>>();			
				
				maxOnts = (olts.size() / 42) + 1;
				
				for (int i = 0; i < olts.size(); i += maxOnts) {
					Integer limMax = i + maxOnts;
					if (limMax >= olts.size()) {
						limMax = olts.size();
					}
					List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(
							olts.subList(i, limMax));

					CompletableFuture<String> executeProcess = poleoMetricas.executeProcess(listSegment, idMonitorPoleo, 1);
					regionSegmentOnts.add(executeProcess);
				}

				CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();
				
				List<OntsConfiguracionDto> ontsEmpresariales =  poleoMetricas.getOntsEmpreariales(201,idMonitorPoleo, true);
				
				ArrayList<CompletableFuture<String>> regionSegmentOntsEmpresariales = new ArrayList<CompletableFuture<String>>();
				Integer maxOntsEmpresariales = (ontsEmpresariales.size() /42) + 1;
				
				for (int i = 0; i < ontsEmpresariales.size(); i += maxOntsEmpresariales) {
					log.info("--------------------------------------------"+i+"------------------------------------");
					Integer limMax = i + maxOntsEmpresariales;
					if (limMax >= ontsEmpresariales.size()) {
						limMax = ontsEmpresariales.size();
					}
					
					List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(ontsEmpresariales.subList(i, limMax));
					
					
					CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,idMonitorPoleo, 1); 
					regionSegmentOntsEmpresariales.add(executeProcess);
				}

				CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
				estatus = "EXITOSO";
				
				//cruce de métricas
				poleoMetricas.joinUpdateStatus(idMonitorPoleo);
				
				
				
				
			} catch (Exception e) {
				estatus = "ERRONEO";
			} finally {
				 MonitorActualizacionEstatusEntity monitEstatus = monitorEstatus.getMonitorEstatus(idMonitorPoleo);
				 monitEstatus.setFechaFin(LocalDateTime.now().toString());
				 monitEstatus.setDescripcion(estatus);
				 monitorEstatus.save(monitEstatus);
			}
			

			return "Fin";

		}
	

		

}
