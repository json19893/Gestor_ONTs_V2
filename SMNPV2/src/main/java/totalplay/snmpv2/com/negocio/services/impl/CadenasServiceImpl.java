package totalplay.snmpv2.com.negocio.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import totalplay.snmpv2.com.negocio.services.ICadenaService;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IlimpiezaCadena;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.*;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.*;
import org.springframework.scheduling.annotation.Async;

import totalplay.snmpv2.com.presentacion.MetricaController.MetricaPoleo;


@Slf4j
@Service
public class CadenasServiceImpl extends Constantes implements ICadenaService {
	
	@Autowired
	ICadenasEmpresarialesRepository cadenasRepository;	
	@Autowired
	IpoleoMetricasService poleoMetricas;
	@Autowired
	IinventarioOntsRepository inventarioOnts;
	
	@Override
	public void getCadenas(boolean all) {
		
		List<OntsConfiguracionDto> onts;
		
		try {
			//eliminar empresariales
			try {
				cadenasRepository.deleteEmpresariales();				
			}catch (Exception e) {
				
			}
			
			
			if(all) {
				onts =  inventarioOnts.findOntsEmpresarialesEstatus();
			}else {
				onts =  inventarioOnts.findFaltantesCadenaEmpresariales();
			}
			
			List<CompletableFuture<String>> regionSegmentOnts =new ArrayList<CompletableFuture<String>>();
			Integer maxOnts = (onts.size() /20) + 1;
			
			for (int i = 0; i < onts.size(); i += maxOnts) {
				Integer limMax = i + maxOnts;
				if (limMax >= onts.size()) {
					limMax = onts.size();
				}
				List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(onts.subList(i, limMax));

				CompletableFuture<String> executeProcess = poleoMetricas.getCadenaEmpresariales(listSegment);
				regionSegmentOnts.add(executeProcess);
			}
			
			CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();
			
			
		} catch (Exception e) {
			log.info(e.toString());
		}
	}
    
	@Override
	@Async("taskExecutor2")
	public CompletableFuture<String> runCommands(Integer idMetrica){ 
		List<String> onts	= cadenasRepository.getOntsByMetrica("metrica_"+idMetrica); 
		
		List<CompletableFuture<String>> regionSegmentOnts =new ArrayList<CompletableFuture<String>>();
		
		
		log.info("-------------procesando------------------"+onts.size());
		
		Integer maxOnts = (onts.size() /30)+1 ;
		
		for (int j = 0; j < onts.size(); j += maxOnts) {
			Integer limMax = j + maxOnts;
			if (limMax >= onts.size()) {
				limMax = onts.size();
			}
			List<String> listSegment = new ArrayList<String>(onts.subList(j, limMax));

			CompletableFuture<String> executeProcess = poleoMetricas.poleoEmpresarialesByShell(listSegment, idMetrica, "PRUEBAS");
			regionSegmentOnts.add(executeProcess);
		}		

		CompletableFuture.allOf(regionSegmentOnts.toArray(new CompletableFuture[regionSegmentOnts.size()])).join();
		
		return null;
	}
      
}