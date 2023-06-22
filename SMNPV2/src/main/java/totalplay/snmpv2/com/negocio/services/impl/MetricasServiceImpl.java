package totalplay.snmpv2.com.negocio.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.PoleoMetricasUtilsService;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.*;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IMetricasService;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.*;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.*;
import org.springframework.scheduling.annotation.Async;

import totalplay.snmpv2.com.presentacion.MetricaController.MetricaPoleo;


@Slf4j
@Service
public class MetricasServiceImpl extends Constantes implements IMetricasService {
    
	@Autowired
	IconfiguracionMetricaRepository confMetricas;
	@Autowired
	ImonitorPoleoMetricaRepository monitorMetrica;
	@Autowired
	IpoleoMetricasService poleoMetricas;
	@Autowired
	IinventarioOntsRepository inventarioOnts;
	
	Utils util = new Utils();
    
    @Override
    @Async("taskExecutor2")
    public CompletableFuture<String> poleoMetricas(int idMetrica,String idMonitorPoleo,  List<CatOltsEntity> olts, boolean snmpBulkWalk, boolean reprocesoEmpresariales ) {
		
    	List <OntsConfiguracionDto> ontsEmpresariales;
		List<CompletableFuture<String>> regionSegmentOnts;
		List<CompletableFuture<String>> regionSegmentOntsEmpresariales;
		Integer maxOntsEmpresariales;
		int activas = confMetricas.getCountActive(idMetrica);
		int j = idMetrica;
		
		try {
			if(activas>0){
				
				MonitorPoleoMetricaEntity monitPoleoMetrica = monitorMetrica.save(new MonitorPoleoMetricaEntity(Integer.valueOf(j),util.getDate(),idMonitorPoleo));
				String idMonitorMetrica = monitPoleoMetrica.getId(); 
				//monitPoleoMetrica = monitorMetrica.getMonitorMetrica(idMonitorMetrica);
				if(snmpBulkWalk) {
					regionSegmentOnts = new ArrayList<CompletableFuture<String>>();			
					
					Integer maxOnts = (olts.size()/110) + 1;
					
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
				if(snmpBulkWalk)
					ontsEmpresariales =  poleoMetricas.getOntsFaltantes(j,idMonitorPoleo, true, reprocesoEmpresariales, "auxiliar", 2, null, null);
				else
					ontsEmpresariales =  inventarioOnts.findOntsEmpresarialesEstatus();
				
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

		} catch (Exception e) {
			log.info(e.toString());
		}
		
		return null;
		
	}
	
    
    
    
}