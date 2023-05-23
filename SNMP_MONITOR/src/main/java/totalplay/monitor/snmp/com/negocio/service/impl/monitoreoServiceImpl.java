package totalplay.monitor.snmp.com.negocio.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.monitor.snmp.com.negocio.dto.catRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.diferenciasDto;
import totalplay.monitor.snmp.com.negocio.dto.responseMonitoreo;
import totalplay.monitor.snmp.com.negocio.dto.responseRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.tbHistoricoDto;
import totalplay.monitor.snmp.com.negocio.dto.totalGraficaDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOltsDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOntsEmpDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesTecnologiaDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.ImonitoreoEjecucionService;
import totalplay.monitor.snmp.com.negocio.util.utils;
import totalplay.monitor.snmp.com.persistencia.entidad.catRegionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.tbHistoricoDiferenciasEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.tblMonitoreoEjecucionEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IcatRegionesRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IdetalleActualizacionRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IdiferenciasManualRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOltsReposirorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsPdmRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ItabHistoricoDiferenciasRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ItblDiferenciasRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ItblMonitoreoEjecucionRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsEmpresarialesRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsVipsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalRegionRepositorio;

@Service
@Slf4j
public class monitoreoServiceImpl extends utils implements ImonitorService {
	@Autowired
	IinventarioOltsReposirorio invOLts;
	@Autowired
	IcatRegionesRepositorio catRegiones;
	@Autowired
	ItabHistoricoDiferenciasRepositorio historicoDiferencias;
	@Autowired
	ImonitoreoEjecucionService monitoreoEjecucion;
	@Autowired
	ItblDiferenciasRepositorio dieferenciasRepositori;
	@Autowired
	IcatOltsRepositorio catOltsRepositorio;
	@Autowired
	IvwTotalRegionRepositorio totalOlts;
	@Autowired
	IinventarioOntsRepositorio inventario;
	@Autowired
	IvwTotalOntsRepositorio vwOnts;
	@Autowired
	ItblMonitoreoEjecucionRepositorio monitorEjecucion;
	@Autowired
	IinventarioOntsPdmRepositorio inventarioPdm;
	@Autowired
	IvwTotalOntsEmpresarialesRepositorio wvOntsEmp; 
	@Autowired
	IvwTotalOntsVipsRepositorio wvOntsVips; 
	@Autowired
	IdiferenciasManualRepository diferencias;
	@Autowired
    IdetalleActualizacionRepositorio detalleAct;

	@Override
	public responseRegionDto getOltsByRegion(Integer idRegion, String tipo, boolean onlyHeaders) throws Exception {
		responseRegionDto response = new responseRegionDto();

		Integer arribaHuawei = 0;
		Integer abajoHuawei = 0;
		Integer abajoHuawe = 0;
		Integer arribaZte = 0;
		Integer abajoZte = 0;
		Integer abajoZt = 0;
		Integer arribaFh = 0;
		Integer abajoFh = 0;
		Integer abajoF = 0;
		List<totalesTecnologiaDto> totalesRegion = null;
		List<totalesOntsEmpDto> result = null;

		if (tipo.compareTo("T") == 0) {
			arribaHuawei = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "HUAWEI", 1);
			abajoHuawei = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "HUAWEI", 2);
			abajoHuawe = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "HUAWEI", 0);
			arribaZte = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "ZTE", 1);
			abajoZte = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "ZTE", 2);
			abajoZt = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "ZTE", 0);
			arribaFh = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "FIBER HOME", 1);
			abajoFh = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "FIBER HOME", 2);
			abajoF = catOltsRepositorio.findCatOltsByStatusTecnologiaRegionCount(idRegion, "FIBER HOME", 0);
		} else if(tipo.equals("E")){
			totalesRegion = catOltsRepositorio.getTotalesTecnologiaRegion(idRegion);
		} else if(tipo.equals("V")){
			totalesRegion = catOltsRepositorio.getTotalesTecnologiaRegionVips(idRegion);
		} else {
			totalesRegion = catOltsRepositorio.getTotalesTecnologiaRegionServiciosAdministrados(idRegion);
		}
		
		result = inventario.getConteoByEmp(idRegion);

		if (tipo.compareTo("T") == 0) {
			response.setTotalAbajoHuawei(abajoHuawei + abajoHuawe);
			response.setTotalAbajoZte(abajoZte + abajoZt);
			response.setTotalAbajoFh(abajoFh + abajoF);
			response.setTotalArribaHuawei(arribaHuawei);
			response.setTotalArribaZte(arribaZte);
			response.setTotalArribaFh(arribaFh);
			response.setTotalHuawei(response.getTotalAbajoHuawei() + response.getTotalArribaHuawei());
			response.setTotalZte(response.getTotalAbajoZte() + response.getTotalArribaZte());
			response.setTotalFh(response.getTotalAbajoFh() + response.getTotalArribaFh());
		} else {
			for (totalesTecnologiaDto tecnologia : totalesRegion) {
				switch (tecnologia.getTecnologia()) {
				case "HUAWEI":
					response.setTotalHuawei(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
					response.setTotalArribaHuawei(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
					response.setTotalAbajoHuawei(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
					break;
				case "ZTE":
					response.setTotalZte(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
					response.setTotalArribaZte(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
					response.setTotalAbajoZte(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
					break;
				case "FIBER HOME":
					response.setTotalFh(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
					response.setTotalArribaFh(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
					response.setTotalAbajoFh(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
					break;

				}
			}
			if (response.getTotalHuawei() == null) {
				response.setTotalHuawei(0);
				response.setTotalArribaHuawei(0);
				response.setTotalAbajoHuawei(0);
			}
			if (response.getTotalZte() == null) {
				response.setTotalZte(0);
				response.setTotalArribaZte(0);
				response.setTotalAbajoZte(0);
			}
			if (response.getTotalFh() == null) {
				response.setTotalFh(0);
				response.setTotalArribaFh(0);
				response.setTotalAbajoFh(0);
			}
		}
		
		if(result != null) {
			
			totalesOntsEmpDto tecnologia =new totalesOntsEmpDto();
			tecnologia.setTecnologia("HUAWEI");
			Integer huawei= result.indexOf(tecnologia);
			tecnologia.setTecnologia("ZTE");
			Integer zte= result.indexOf(tecnologia);
			tecnologia.setTecnologia("FIBER HOME");
			Integer fiber= result.indexOf(tecnologia);
			
			
			
			response.setTotalAbajoHuaweiEmp( huawei != -1 ? result.get(huawei).getAbajo() + result.get(huawei).getSin_informacion():0 );
			response.setTotalAbajoZteEmp(zte != -1 ? result.get(zte).getAbajo() + result.get(zte).getSin_informacion():0);
			response.setTotalAbajoFhEmp(fiber != -1 ? result.get(fiber).getAbajo() + result.get(fiber).getSin_informacion():0);
			response.setTotalArribaHuaweiEmp(huawei != -1 ? result.get(huawei).getArriba():0);
			response.setTotalArribaZteEmp(zte != -1 ? result.get(zte).getArriba():0);
			response.setTotalArribaFhEmp(fiber != -1 ? result.get(fiber).getArriba():0);
			response.setTotalHuaweiEmp(huawei != -1 ? result.get(huawei).getTotal():0);
			response.setTotalZteEmp(zte != -1 ? result.get(zte).getTotal():0);
			response.setTotalFhEmp(fiber != -1 ? result.get(fiber).getTotal():0);
		}

			

		if(!onlyHeaders) {
			if (tipo.equals("T")) 
				response.setTotalesRegion(catOltsRepositorio.getDataRegion(idRegion)); /*vwOnts.findByIdRegion(idRegion));*/
			else if (tipo.equals("E"))
				response.setTotalesRegionEmp(catOltsRepositorio.getDataRegionEmpresariales(idRegion));
			else if(tipo.equals("V"))
				response.setTotalesRegionVips(catOltsRepositorio.getDataRegionVips(idRegion));
			else
				response.setTotalesRegionSA(catOltsRepositorio.getDataRegionSA(idRegion));
		}	

		return response;
	}
	
	@Override
	public List<catRegionDto> getRegion() throws Exception {
		List<catRegionDto> response = new ArrayList<catRegionDto>();

		try {
			List<catRegionEntidad> cat = catRegiones.findAll();
			for (catRegionEntidad c : cat) {
				catRegionDto res = new catRegionDto();
				BeanUtils.copyProperties(res, c);
				response.add(res);
			}

		} catch (Exception e) {
		log.error("error", e);
		}

		return response;
	}

	@Override
	public List<inventarioOntsEntidad> getOntsByOlts(Integer idOlt, Integer estatus, String tipo) throws Exception {
		if(tipo.compareTo("T")==0){
			return invOLts.finOntsByIdOlts(idOlt, estatus);
		}else if(tipo.compareTo("E")==0){
			return invOLts.finOntsByIdOltsEmp(idOlt, estatus);
		}else {
			return invOLts.finOntsByIdOltsVips(idOlt, estatus);
		}
			
	}

	@Override
	public List<inventarioOntsEntidad> finOntsByIdAll(Integer idOlt, String tipo) throws Exception {
		List<inventarioOntsEntidad> onts=new ArrayList<inventarioOntsEntidad>();
		try {
			
				if(tipo.compareTo("T")==0) {
					onts= invOLts.finOntsByIdAll(idOlt);
				}else if (tipo.compareTo("E")==0) {
					onts= invOLts.finOntsByIdAllEmp(idOlt);
				}else {
					onts= invOLts.finOntsByIdAllVips(idOlt);
				}
	} catch (Exception e) {
		log.error("error", e);
	}
	return onts;
	}

	@Override
	public List<tbHistoricoDto> getHistoricoCambios(Integer idOlt, String tipo) throws Exception {
		/*
		 * -1--sin informaci�n a 1 -2--sin informaci�n a 2 -3--1 a 2 1--1 a sin
		 * informaci�n 2--2 a 0 3--2 a 1 *
		 */
		List<tbHistoricoDto> response = new ArrayList<tbHistoricoDto>();
		List<tbHistoricoDiferenciasEntidad> historico = null;
		if(tipo.compareTo("T")==0) {
			historico = historicoDiferencias.findHistoricoCambios(idOlt);
		}else if(tipo.compareTo("T")==0){
			historico = historicoDiferencias.findHistoricoCambiosEmp(idOlt);
		}else {
			historico = historicoDiferencias.findHistoricoCambiosVips(idOlt);
		}
		for (tbHistoricoDiferenciasEntidad h : historico) {
			tbHistoricoDto r = new tbHistoricoDto();
			r.set_id(h.get_id());
			r.setNumero_serie(h.getNumero_serie());

			r.setFecha_descubrimiento(h.getFecha_descubrimiento());
			r.setId_olt(h.getId_olt());
			r.setOid(h.getOid());
			r.setAlias(h.getAlias());
			r.setPort(h.getPort());
			r.setSlot(h.getSlot());
			r.setFrame(h.getFrame());
			r.setDescripcionAlarma(h.getDescripcionAlarma());
			r.setLastDownTime(h.getLastDownTime());
			String u = h.getOid().trim().toString();
			u = u.replace(".", "=");
			String[] uid = u.split("=");
			if (uid.length > 1) {
				r.setUid(uid[1]);
			} else {
				r.setUid("--");
			}
			r.setTipo(h.getTipo());
			if (!h.getDiferencias().isEmpty()) {
				String cadena = "";
				for (diferenciasDto f : h.getDiferencias()) {

					if (f.getId_olt() != h.getId_olt()) {
						cadena = cadena + " " + f.getId_olt();

					}
				}
				r.setTipoCambio("Existe en las OLTS: " + cadena);

			}
			switch (h.getEstatus()) {
			case "-1":
				r.setTipoCambio("Nueva ONT: ARRIBA");
				break;
			case "-2":
				r.setTipoCambio("Nueva ONT: ABAJO");
				break;
			case "-3":
				r.setTipoCambio("ARRIBA a ABAJO");
				break;
			case "1":
				r.setTipoCambio("ARRIBA a Desconectada");
				break;
			case "2":
				r.setTipoCambio("ABAJO a Desconectada");
				break;
			case "3":
				r.setTipoCambio("ABAJO a ARRIBA");
				break;
			}

			response.add(r);
		}

		return response;
	}

	@Override
	public List<datosRegionDto> getTotalesByTecnologia(String tipo) throws Exception {
		try {	
			if (tipo.compareTo("T") == 0) {
				return inventario.getTotalesRegion();
			} else if(tipo.compareTo("E") == 0) {
				return inventario.getTotalesEmpresariales();
			}else {
				return inventario.getTotalesRegionesVips();
			}
		}catch(Exception e) {
			log.error("error", e);
		}
		
		return new ArrayList<datosRegionDto>();
	}

	@Override
	@Async("taskExecutor")
	public CompletableFuture<datosRegionDto> getConteo(Integer idRegion, String region) {
		System.out.println("get poleo olt :" + Thread.currentThread().getName());
		System.out.println("prioridad :" + Thread.currentThread().getPriority());
		System.out.println("activeCount :" + Thread.activeCount());
		datosRegionDto r = new datosRegionDto();
		r.setIdRegion(idRegion);
		r.setRegion(region);
		r.setTotalRegion(catOltsRepositorio.findCaTotales(idRegion));
		r.setTotalOnt(inventario.finOntsByTotalRegion(idRegion));
		r.setAbajo(inventario.finOntsByEstatusRegion(idRegion, 2));
		r.setArriba(inventario.finOntsByEstatusRegion(idRegion, 1));
		r.setTotalOlt(catOltsRepositorio.findCatActivos(idRegion));
		r.setCambios(historicoDiferencias.findTotalCambios(idRegion));
		return CompletableFuture.completedFuture(r);
	}

	@Override
	public totalesActivoDto getTotalesActivo(String tipo) throws Exception {
		totalesActivoDto response = new totalesActivoDto();
		response.setTotalHuawei(new Integer(0));
		response.setTotalZte(new Integer(0));
		response.setTotalFh(new Integer(0));
		List<totalesTecnologiaDto> totalesRegion = null;
		List<totalesOntsEmpDto> result = null;
		Integer totalOnts = 0;
		Integer total=0;
		try {
			
	
		if (tipo.compareTo("T") == 0) {
			totalesRegion = catOltsRepositorio.getTotalesTecnologiaT();
		}else if(tipo.compareTo("E") == 0 ){
			totalesRegion = catOltsRepositorio.getTotalesTecnologia();
		}else {
			totalesRegion = catOltsRepositorio.getTotalesTecnologiaVips();
		}

		result = inventario.getAllOntEmp();
		
		if(result != null) {
			
			totalesOntsEmpDto tecnologia =new totalesOntsEmpDto();
			tecnologia.setTecnologia("HUAWEI");
			Integer huawei= result.indexOf(tecnologia);
			tecnologia.setTecnologia("ZTE");
			Integer zte= result.indexOf(tecnologia);
			tecnologia.setTecnologia("FIBER HOME");
			Integer fiber= result.indexOf(tecnologia);
			
			
			
			response.setTotalAbajoHuaweiEmp( huawei != -1 ? result.get(huawei).getAbajo() + result.get(huawei).getSin_informacion():0 );
			response.setTotalAbajoZteEmp(zte != -1 ? result.get(zte).getAbajo() + result.get(zte).getSin_informacion():0);
			response.setTotalAbajoFhEmp(fiber != -1 ? result.get(fiber).getAbajo() + result.get(fiber).getSin_informacion():0);
			response.setTotalArribaHuaweiEmp(huawei != -1 ? result.get(huawei).getArriba():0);
			response.setTotalArribaZteEmp(zte != -1 ? result.get(zte).getArriba():0);
			response.setTotalArribaFhEmp(fiber != -1 ? result.get(fiber).getArriba():0);
			response.setTotalHuaweiEmp(huawei != -1 ? result.get(huawei).getTotal():0);
			response.setTotalZteEmp(zte != -1 ? result.get(zte).getTotal():0);
			response.setTotalFhEmp(fiber != -1 ? result.get(fiber).getTotal():0);
		}
		
		Integer totaEmpresarial = response.getTotalHuaweiEmp()+response.getTotalZteEmp()+response.getTotalFhEmp();
		
		for (totalesTecnologiaDto tecnologia : totalesRegion) {
			if(tecnologia.getTecnologia() == null){
				continue;
			}
			switch (tecnologia.getTecnologia()) {
			case "HUAWEI":
				response.setTotalHuawei(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
				response.setTotalArribaHuawei(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
				response.setTotalAbajoHuawei(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
				break;
			case "ZTE":
				response.setTotalZte(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
				response.setTotalArribaZte(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
				response.setTotalAbajoZte(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
				break;
			case "FIBER HOME":
				response.setTotalFh(tecnologia.getTotal() != null ? tecnologia.getTotal() : 0);
				response.setTotalArribaFh(tecnologia.getArriba() != null ? tecnologia.getArriba() : 0);
				response.setTotalAbajoFh(tecnologia.getAbajo() != null ? tecnologia.getAbajo() : 0);
				break;

			}
		}
		if (response.getTotalZte() == null) {
			response.setTotalZte(0);
			response.setTotalArribaZte(0);
			response.setTotalAbajoZte(0);
		}
		if (response.getTotalFh() == null) {
			response.setTotalFh(0);
			response.setTotalArribaFh(0);
			response.setTotalAbajoFh(0);
		}
		

		List<tblMonitoreoEjecucionEntidad> fe = monitorEjecucion.findAll();
		response.setUltimaActualizacion(fe.get(0).getFecha_fin());
		
	     Date fecha=fe.get(0).getFecha_fin();
	 
		   Instant instant = fecha.toInstant();
		   Instant nextDay = instant.plus(1, ChronoUnit.DAYS);
	       response.setProximoDescubrimiento(Date.from(nextDay));
		   Date fechaDia = new Date();//format.parse( LocalDate.now().toString()+"T00:00:00.000Z");
		   fechaDia.setHours(0);
		   fechaDia.setMinutes(0);
		   fechaDia.setSeconds(0);
		   log.info("###################################fechaaa #######################################"+fechaDia.toString());
	    if(tipo.equals("E")) {
           response.setConteoPdmOnts(detalleAct.getDetalleEmpresariales(fechaDia).size());
		   log.info("###################################total emp #######################################"+response.getConteoPdmOnts());
           totalOnts= totaEmpresarial;
	    }else if(tipo.equals("V")) {
			response.setConteoPdmOnts(detalleAct.getDetalleVips(fechaDia).size());
			log.info("###################################total vip #######################################"+response.getConteoPdmOnts());
	    	totalOnts =  inventario.finOntsByClasificionV();

	    }else {
	    	 totalOnts = inventario.finOntsByTotal()+inventarioPdm.finOntsByTotalT();
	    	 ///response.setConteoPdmOnts( inventarioPdm.finOntsByTotalT()+ inventario.findTotalCambiosT());
			 response.setConteoPdmOnts(detalleAct.getDetalle(fechaDia).size());
	    }
	
		
		total	 = response.getTotalHuawei().intValue() + response.getTotalZte().intValue()  + response.getTotalFh().intValue() ;
		List<totalGraficaDto> grafica = new ArrayList<totalGraficaDto>();
		for (int i = 1; i <= 3; i++) {
			totalGraficaDto t = new totalGraficaDto();
			switch (i) {
			case 1:
				t.setCategory("Emp");
				t.setValue(totaEmpresarial);
				t.setFull(100);
				break;

			case 2:
				t.setCategory("ONTs");				
				t.setValue(totalOnts);
				t.setFull(100);
				break;
			case 3:
				t.setCategory("OLTs");
				t.setValue(total);
				t.setFull(100);
				break;

			}
			grafica.add(t);
		}
		response.setGrafica(grafica);
	} catch (Exception e) {
		log.error("Error:  ", e);
	}
		return response;
	}

	@Override
	public responseMonitoreo getDatosMonitoreo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public totalesOltsDto getTotalesByOlt(Integer idOlt, String tipo) throws Exception {
		totalesOltsDto response = new totalesOltsDto();
		try {
			response.setCambios(diferencias.findTotalCambios(idOlt));
			if(tipo.compareTo("T")==0) {
				response.setTotalOlt(inventario.finOntsByTotalOlt(idOlt)+inventarioPdm.finOntsByTotalOlt(idOlt) );
				response.setArriba(inventario.finOntsByTotalEstatus(idOlt, 1)+inventarioPdm.finOntsByTotalEstatus(idOlt, 1));
				response.setAbajo(inventario.finOntsByTotalEstatus(idOlt, 2)+inventario.finOntsByTotalEstatus(idOlt, 0)+inventarioPdm.finOntsByTotalEstatus(idOlt, 0)+inventarioPdm.finOntsByTotalEstatus(idOlt, 2));
				//response.setCambios(historicoDiferencias.findTotalCambiosOlt(idOlt));
				
			}else if(tipo.compareTo("E")==0){
				response.setTotalOlt(inventario.finOntsByTotalOltEmp(idOlt));
				response.setArriba(inventario.finOntsByTotalEstatusEmp(idOlt, 1));
				response.setAbajo(inventario.finOntsByTotalEstatusEmp(idOlt, 2));
				//response.setCambios(historicoDiferencias.findTotalCambiosOltEmp(idOlt));
				
			}else {
				response.setTotalOlt(inventario.finOntsByTotalOltVip(idOlt));
				response.setArriba(inventario.finOntsByTotalEstatusVip(idOlt, 1));
				response.setAbajo(inventario.finOntsByTotalEstatusVip(idOlt, 2));
				//response.setCambios(historicoDiferencias.findTotalCambiosOltVip(idOlt));
				
			}
			
		} catch (Exception e) {
			log.error("error", e);
		}
		return response;
	}

}