package totalplay.snmp.com.negocio.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import totalplay.snmp.com.configuracion.utils.constantes;
import totalplay.snmp.com.configuracion.utils.utils;
import totalplay.snmp.com.negocio.dto.configuracioDto;
import totalplay.snmp.com.negocio.dto.ejecucionDto;
import totalplay.snmp.com.negocio.dto.responseDto;
import totalplay.snmp.com.negocio.service.ImonitoreoOltService;
import totalplay.snmp.com.negocio.service.ImonitoreoRegionService;
import totalplay.snmp.com.negocio.service.IpoleoDropDownPacketsService;

import totalplay.snmp.com.persistencia.repositorio.IpoleoMetricaRepositorio;

@Service
public class poleoDropDownPacketsServiceImpl extends constantes implements IpoleoDropDownPacketsService {

	@Autowired
	IpoleoMetricaRepositorio poleoMetrica;
	@Autowired
	ImonitoreoOltService monitOlt;

	utils utl = new utils();

	/*
	 * Metodo para ejecutar el poleo de Last Down Cause
	 */
	@Override
	@Async("taskExecutor")
	public CompletableFuture<responseDto> getDropDownPackets(configuracioDto configuracion, String idMonitoreoOlt,
			String fechaInicio, String idRegion, String IdMonitoreoEjecucion) throws Exception {
		responseDto response = new responseDto();
		List<poleosEntidad> metricas = new ArrayList<poleosEntidad>();
		try {
			monitOlt.updateMonitorOlt(idRegion, idMonitoreoOlt,
					EN_PROCESO_DESC + POLEO + EJECUCION_DROP_DOWN_PACKETS, fechaInicio.toString(), "", CURSO, configuracion.getIdOlt());
			System.out.println("PoleoMemoria :" + Thread.currentThread().getName());
			System.out.println("prioridad :" + Thread.currentThread().getPriority());
			System.out.println("activeCount :" + Thread.activeCount());

			ejecucionDto proces = utl
					.ejecutaComando(configuracion.getComando() + SPACE + "hwGponOntStatisticDownDropPackts");

			if(proces.getBuffer() != null) {
				metricas = utl.getMetricasBypoleo(proces, 11, configuracion.getIdOlt(), IdMonitoreoEjecucion);
	
				proces.getProceso().waitFor();
				System.out.println("valor del proceso: " + proces.getProceso().exitValue());
				proces.getProceso().destroy();
				poleoMetrica.saveAll(metricas);
	
				monitOlt.updateMonitorOlt(idRegion, idMonitoreoOlt,
						PROCESO + POLEO + EJECUCION_DROP_DOWN_PACKETS + FINAL_EXITO, fechaInicio,
						LocalDateTime.now().toString(), TERMINO_EXITOSO, configuracion.getIdOlt());
				response.setSms("Exito al descubir memoria");
				response.setCod(0);
			}else {
				monitOlt.updateMonitorOlt(idRegion, idMonitoreoOlt,
						PROCESO + POLEO + EJECUCION_DROP_DOWN_PACKETS + FINAL_ERROR , fechaInicio,
						LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());
				response.setSms("Error al ejecutar el comando.");
				response.setCod(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: " + e);
			response.setSms("Error: " + e);
			response.setCod(1);
			monitOlt.updateMonitorOlt(idRegion, idMonitoreoOlt,
					PROCESO + POLEO + EJECUCION_DROP_DOWN_PACKETS + FINAL_ERROR + " : " + e, fechaInicio,
					LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());

		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error: " + e);
			response.setSms("Error: " + e);
			response.setCod(1);
			monitOlt.updateMonitorOlt(idRegion, idMonitoreoOlt,
					PROCESO + POLEO + EJECUCION_DROP_DOWN_PACKETS + FINAL_ERROR + " : " + e, fechaInicio,
					LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());
		}
		return CompletableFuture.completedFuture(response);
	}

}
