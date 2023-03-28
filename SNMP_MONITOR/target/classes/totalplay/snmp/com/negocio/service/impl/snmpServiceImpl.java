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
import totalplay.snmp.com.negocio.service.IsnmpService;
import totalplay.snmp.com.persistencia.entidades.descubrimientoTemporalEntidad;
import totalplay.snmp.com.persistencia.repositorio.IpoleoMetricaRepositorio;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoRegionRepositorio;
import totalplay.snmp.com.persistencia.repositorio.IsnmpRepositorio;

@Service
public class snmpServiceImpl extends constantes implements IsnmpService {
	@Autowired
	IsnmpRepositorio repositorioSnmp;
	@Autowired
	ImonitoreoRegionRepositorio monitoreoRegion;
	@Autowired
	IpoleoMetricaRepositorio memoriaRepositorio;
	@Autowired
	ImonitoreoOltService monitOlt;

	utils utl = new utils();

	/*
	 * Metodo para ejecutar el descubrimiento de las ONTS
	 */
	@Override
	@Async("taskExecutor")
	public CompletableFuture<responseDto> descubrimiento(configuracioDto configuracion, String idMonitoreoRegion,
			String fechaInicio, String idRegion, String IdMonitoreoEjecucion) throws Exception {
		System.out.println("descubrimiento :" + Thread.currentThread().getName());
		System.out.println("prioridad :" + Thread.currentThread().getPriority());
		System.out.println("activeCount :" + Thread.activeCount());

		responseDto response = new responseDto();
		List<descubrimientoTemporalEntidad> desc = new ArrayList<descubrimientoTemporalEntidad>();
		String s;
		try {

			monitOlt.updateMonitorOlt(idRegion, idMonitoreoRegion,
					EN_PROCESO_DESC + DESCUBRIMIENTO + EJECUCION_ONT, fechaInicio.toString(), "", CURSO, configuracion.getIdOlt());
			ejecucionDto proces = utl.ejecutaComando(configuracion.getComando() + SPACE + "hwGponDeviceOntSn");
			if(proces.getBuffer() != null){
				while ((s = proces.getBuffer().readLine()) != null) {
					System.out.println("descubrimiento valores :" + s);
					descubrimientoTemporalEntidad de = new descubrimientoTemporalEntidad();
					String value = s.replaceAll(SPLIT_NUM_SERIE_CADENA, "");
					String[] valOid = value.split(SPLIT_OID);
					String[] val = s.split(SPLIT_NUM_SERIE_VAL2);
					if (val.length == 1) {
						val = s.split(SPLIT_NUM_SERIE_VAL2);
					}
					if (val.length == 2) {
						de.setNumero_serie(val[1].trim().replaceAll("'", "").replaceAll(" ", ""));
					} else {
						de.setNumero_serie(val[val.length - 1].trim().replaceAll(" ", ""));
					}
					de.setEstatus(1);
					de.setOid(valOid[0].trim());
					de.setId_olts(Integer.parseInt(configuracion.getIdOlt()));
					de.setFecha_descubrimiento(LocalDateTime.now().toString());
					de.setId_ejecucion(IdMonitoreoEjecucion);
					de.setId_region(Integer.parseInt(configuracion.getIdRegion()));
					desc.add(de);
				}
				proces.getProceso().waitFor();
				System.out.println("valor del proceso: " + proces.getProceso().exitValue());
				proces.getProceso().destroy();
				repositorioSnmp.saveAll(desc);
				// getMetricaMemory(configuracion.getComando(), configuracion.getIdOlt());
				monitOlt.updateMonitorOlt(idRegion, idMonitoreoRegion,
						PROCESO + DESCUBRIMIENTO + EJECUCION_ONT + FINAL_EXITO, fechaInicio, LocalDateTime.now().toString(),
						TERMINO_EXITOSO, configuracion.getIdOlt());
	
				response.setSms("Ejecutar sms");
				response.setCod(0);
			}else {
				monitOlt.updateMonitorOlt(idRegion, idMonitoreoRegion,
						PROCESO + DESCUBRIMIENTO + EJECUCION_ONT + FINAL_ERROR + " : " + proces.getError(), fechaInicio,
						LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());
				response.setSms("Error: " + proces.getError());
				response.setCod(1);
			}			

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: " + e);
			response.setSms("Error: " + e);
			response.setCod(1);
			monitOlt.updateMonitorOlt(idRegion, idMonitoreoRegion,
					PROCESO + DESCUBRIMIENTO + EJECUCION_ONT + FINAL_ERROR + " : " + e, fechaInicio,
					LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error: " + e);
			response.setSms("Error: " + e);
			response.setCod(1);
			monitOlt.updateMonitorOlt(idRegion, idMonitoreoRegion,
					PROCESO + DESCUBRIMIENTO + EJECUCION_ONT + FINAL_ERROR + " : " + e, fechaInicio,
					LocalDateTime.now().toString(), TERMINO_ERRO, configuracion.getIdOlt());
		}
		return CompletableFuture.completedFuture(response);
	}
}
