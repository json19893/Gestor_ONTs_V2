package totalplay.snmpv2.com.negocio.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.negocio.services.ICronUpdateService;
import totalplay.snmpv2.com.negocio.services.IUpdateTotalOntsService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.VwTotalOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.ITotalOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class CronUpdateServiceImpl implements ICronUpdateService{
	
	@Autowired
	IinventarioOntsRepository ontsRepository;
	@Autowired
	IpoleoMetricasService poleoMetricas;
	
	@Override
	public void updateStatusOlt(CatOltsEntity olt) {
		
	
		List<OntsConfiguracionDto> ontsEmpresariales =  ontsRepository.getEmpresarialesByOlt(olt.getId_olt());
		
		ArrayList<CompletableFuture<String>> regionSegmentOntsEmpresariales = new ArrayList<CompletableFuture<String>>();
		Integer maxOntsEmpresariales = (ontsEmpresariales.size() /42) + 1;
		
		for (int i = 0; i < ontsEmpresariales.size(); i += maxOntsEmpresariales) {
			log.info("--------------------------------------------"+i+"------------------------------------");
			Integer limMax = i + maxOntsEmpresariales;
			if (limMax >= ontsEmpresariales.size()) {
				limMax = ontsEmpresariales.size();
			}
			
			List<OntsConfiguracionDto> listSegment = new ArrayList<OntsConfiguracionDto>(ontsEmpresariales.subList(i, limMax));
			
			
			CompletableFuture<String> executeProcess = poleoMetricas.getMetricaEmpresarialesByMetrica( listSegment,"", 1, true); 
			regionSegmentOntsEmpresariales.add(executeProcess);
		}
		
		CompletableFuture.allOf(regionSegmentOntsEmpresariales.toArray(new CompletableFuture[regionSegmentOntsEmpresariales.size()])).join();
		
	}

}

