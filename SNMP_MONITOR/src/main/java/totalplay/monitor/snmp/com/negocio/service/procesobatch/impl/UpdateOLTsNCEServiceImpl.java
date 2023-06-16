package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarOntsByTecnologiaDto;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IObtenerOntsByTecnologiaService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IUpdateOLTsNCEService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.TotalesByTecnologiaEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.ITotalesByTecnologiaRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;

@Service
public class UpdateOLTsNCEServiceImpl implements IUpdateOLTsNCEService {
    @Autowired
    IcatOltsRepositorio catOlts;
    
    
	@Override
	//@Scheduled(fixedDelay = 10000)
	public void updateOlts() {
    	//System.out.println("Inicia el proceso de actualización de estatus nce");
    	try {
			Date date =  Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(7,ChronoUnit.DAYS).minus(1,ChronoUnit.HOURS));
			date.setHours(0);
			date.setMinutes(0);
			date.setSeconds(0);
			
			catOlts.updateStatusNCE(date);
    	}catch (Exception e) {
    		//System.out.println("Termina el proceso de actualización de estatus nce de forma FALLIDA");
    		return ;
    	}
		//System.out.println("termina el proceso de actualización de estatus nce");
	}
    
    
    
}
