package totalplay.monitor.snmp.com.negocio.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.monitor.snmp.com.negocio.dto.*;

import totalplay.monitor.snmp.com.negocio.service.IconsultaService;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.util.utils;
import totalplay.monitor.snmp.com.persistencia.entidad.*;

import totalplay.monitor.snmp.com.persistencia.repository.*;

@Service
@Slf4j
public class consultaServiceImpl extends utils implements IconsultaService {

	@Autowired
	IcatOltsRepositorio catalogoOlts;
	@Autowired
	IinventarioOntsRepositorio inventarioOnts;
	@Autowired
	IvwTotalOntsRepositorio vwOnts;
	@Autowired
	IinventarioOltsReposirorio invOnts;
	@Autowired
	ImonitorService monitoreo;
	@Autowired
	IvwTotalOntsEmpresarialesRepositorio vwOntsEmp;
	@Autowired
	IpoleoLastUpTimeRepositorio poleoLastUpTime;
	@Autowired
	IpoleoUpBytesRepositorio poleoUpBytes;
	@Autowired
	IpoleoDownBytesRepositorio poleoDownBytes;
	@Autowired
	IpoleoTimeOutRepositorio poleoTimeOut;
	@Autowired
	IpoleoUpPacketsRepositorio poleoUpPackets;
	@Autowired
	IpoleoDownPacketsRepositorio poleoDownPackets;
	@Autowired
	IpoleoDropUpPacketsRepositorio poleoDropUpPackets;
	@Autowired
	IpoleoDropDownPacketsRepositorio poleoDropDownPackets;
	@Autowired
	IpoleoCpuRepositorio poleoCpu;
	@Autowired
	IpoleoMemoryRepositorio poleoMemory;
	@Autowired
	IpoleoProfNameRepositorio poleoProfName;
	@Autowired
	ImonitorPoleoRepositorio monitor;
	@Autowired
	IdetalleActualizacionRepositorio detalleRepositorio;
	@Autowired
	IinventarioOntsPdmRepositorio ontsPdm;
	@Autowired
	IdiferenciasManualRepository diferencias;
	@Autowired
	IDetalleActualizacionesOltsRepository detalleActualizacionesOltsRepository;
	
	@Value("${ruta.archivo.metrica}")
	private String rutaMetrica;
	@Value("${ruta.archivo.txt}")
	private String rutaDescubrimiento;
	@Value("${ruta.archivo.nce}")
	private String rutaNCE;
	
	@Override
	public Map<String, Object> consultaNumeroSerie(String oid, String ip) {
		HashMap<String, Object> response = new HashMap();
		HashMap<String, Object> data = new HashMap();
		response.put("success", false);
		try {
			catOltsEntidad olt = catalogoOlts.findByIp(ip);
			if (olt == null) {
				response.put("message", "No se encontró la ip en la base.");
				return response;
			}

			inventarioOntsEntidad ont = inventarioOnts.finOntsByOidAndIdOlts(oid, olt.getId_olt());
			if (ont == null) {
				response.put("message", "No se encontró el registro en el inventario.");
				return response;
			}
			data.put("numeroSerie", ont.getNumero_serie());
			response.put("message", "Consulta realizada correctamente");
			response.put("success", true);
			response.put("data", data);

		} catch (Exception e) {
			log.error("error", e);
			response.put("message", "Error al conseguir en nùmero de serie");
		}
		//(olt.getId_olt());
		//(ont.getNumero_serie());
		return response;
	}

	@Override
	public responseFindDto getOlt(String tipo, String data, boolean ip) {
		responseFindDto response = new responseFindDto();
		responseRegionDto responseMonitoreo = new responseRegionDto();
		List<dataRegionResponseDto> oltsVip = new ArrayList<dataRegionResponseDto>();
		List<dataRegionResponseDto> oltsEmp = new ArrayList<dataRegionResponseDto>();
		List<dataRegionResponseDto> olts = new ArrayList<dataRegionResponseDto>();
		Integer idRegion = 0;

		switch (tipo) {
		case "T":
			if (ip) {
				olts = catalogoOlts.getResultLookup(data, true);
			} else {
				olts = catalogoOlts.getResultLookup(data, false);
			}
			if (olts.isEmpty()) {
				response.setMessage("No se pudo localizar la olt");
				return response;
			}
			break;
		case "E":
			if (ip) {
				oltsEmp = catalogoOlts.getResultLookupEmpresarial(data, true);//oltsEmp = vwOntsEmp.findByIp(data);
			} else {
				oltsEmp = catalogoOlts.getResultLookupEmpresarial(data, false);//oltsEmp = vwOntsEmp.findByNombre(data);
			}
			if (oltsEmp.isEmpty()) {
				response.setMessage("No se pudo localizar la olt");
				return response;
			}
			break;
			
		case "V":
			if (ip) {
				oltsVip = catalogoOlts.getResultLookupVip(data, true);
			} else {
				oltsVip = catalogoOlts.getResultLookupVip(data, false);
			}
			if (oltsVip.isEmpty()) {
				response.setMessage("No se pudo localizar la olt");
				return response;
			}
			break;
			
			case "S":
				if (ip) {
					oltsVip = catalogoOlts.getResultLookupSA(data, true);
				} else {
					oltsVip = catalogoOlts.getResultLookupSA(data, false);
				}
				if (oltsVip.isEmpty()) {
					response.setMessage("No se pudo localizar la olt");
					return response;
				}
				break;
		}		

		idRegion = tipo.equals("T") ? olts.get(0).getId_region() : tipo.equals("E") ?  oltsEmp.get(0).getId_region(): oltsVip.get(0).getId_region();

		try {
			responseMonitoreo = monitoreo.getOltsByRegion(idRegion, tipo, true);
			responseMonitoreo.setTotalesRegion(olts);
			responseMonitoreo.setTotalesRegionEmp(oltsEmp);
			responseMonitoreo.setTotalesRegionVips(oltsVip);
		} catch (Exception e) {
			response.setMessage("Error al cargar los datos de la base ");
			log.error("error", e);
			return response;
		}

		response.setSuccess(true);
		response.setMessage("Proceso exitoso.");
		response.setData(responseMonitoreo);
		response.setIdRegion(idRegion);

		return response;
	}
	
	@Override
	public respuestaStatusDto cambiarEstatusOnt(List<requestEstatusDto> datos, String usuario) {
		boolean  datosFaltantes;
		List<requestEstatusDto> noAct=new ArrayList<requestEstatusDto>();
		respuestaStatusDto response = new respuestaStatusDto();
		List<detalleActualizacionesEntidad> actualizadas = new ArrayList<detalleActualizacionesEntidad>();
		List<detalleActualizacionesEntidad> noActualizadas = new ArrayList<detalleActualizacionesEntidad>();
		
		try {
			for (requestEstatusDto d : datos) {
				datosFaltantes=false;
				if(d.getSerie().isEmpty() || d.getFrame()==null || d.getSlot()==null 
						|| d.getPort()==null||d.getTipo().isEmpty()|| d.getOid().isEmpty()||d.getUid().isEmpty()) {
					datosFaltantes=true;
				}
				
				detalleActualizacionesEntidad na = new detalleActualizacionesEntidad();
				catOltsEntidad olt = catalogoOlts.findOltByIp(d.getIp());
				inventarioOntsEntidad res = inventarioOnts.getOntBySerie(d.getSerie());
				inventarioOntsPdmEntidad resPdm = ontsPdm.getOntBySerie(d.getSerie());
				
				if ((res == null && resPdm == null)||datosFaltantes) {
					String causa="";
					if(datosFaltantes)
						causa="Debe de proporcionas todos los datos (frame, slot, port, número de serie)";
					else {
						causa="No existe la ont proporcionada";
					}
					
					na.setNumeroSerie("na");
					na.setIp(d.getIp());
					na.setFrame(d.getFrame());
					na.setSlot(d.getSlot());
					na.setPort(d.getPort());
					na.setDescripcionAlarma("na");
					na.setFechaActualizacion(LocalDateTime.now().toString());
					na.setUid(d.getUid());
					na.setCausa(null);
					na.setCausa(causa);
					na.setUsuario(usuario);
					noActualizadas.add(na);
					d.setMotivo(causa);
					noAct.add(d);
				} else {
					
					if (res!=null) {
						if (d.getEstatus().equals("UP") || d.getEstatus().equals("CLEAR")) {
							res.setEstatus(1);
							na.setCausa("Actualizacion a UP");
						}else {
							res.setEstatus(2);
							na.setCausa("Actualizacion a DOWN");
						}
						
						
						res.setFecha_descubrimiento(Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(6,ChronoUnit.HOURS)));
						res.setActualizacion(6);
						res.setTipo(d.getTipo());
						
						if(olt != null && olt.getId_olt().intValue() == res.getId_olt().intValue()) {
							res.setId_olt(olt.getId_olt());
							res.setFrame(d.getFrame());
							res.setSlot(d.getSlot());
							res.setPort(d.getPort());
							res.setId_puerto(d.getOid());
							res.setUid(d.getUid());
							res.setOid(d.getOid()+d.getUid());
						}
						
						invOnts.save(res);
					} else if(resPdm!=null) {
						
						if (d.getEstatus().equals("UP") || d.getEstatus().equals("CLEAR")) {
							resPdm.setEstatus(1);
							na.setCausa("Actualizacion a UP");
						}else {
							resPdm.setEstatus(2);
							na.setCausa("Actualizacion a DOWN");
						}
						
						
						resPdm.setFecha_descubrimiento(LocalDateTime.now().toString());
						resPdm.setActualizacion(6);
						resPdm.setTipo(d.getTipo());
						
						if(olt !=null &&  olt.getId_olt().intValue() == resPdm.getId_olt().intValue()) {
							resPdm.setId_olt(olt.getId_olt());
							resPdm.setFrame(d.getFrame());
							resPdm.setSlot(d.getSlot());
							resPdm.setPort(d.getPort());
							resPdm.setId_puerto(d.getOid());
							resPdm.setUid(d.getUid());
							resPdm.setOid(d.getOid()+d.getUid());
						}
						
						ontsPdm.save(resPdm);
						
						
						
					}
					
					na.setNumeroSerie(d.getSerie());
					na.setIp(d.getIp());
					na.setFrame(d.getFrame());
					na.setSlot(d.getSlot());
					na.setPort(d.getPort());
					na.setDescripcionAlarma("na");
					na.setFechaActualizacion(LocalDateTime.now().toString());
					na.setUid(d.getUid());
					na.setUsuario(usuario);
					actualizadas.add(na);

				}

			}
			detalleRepositorio.saveAll(actualizadas);
			response.setCod(0);
			response.setSms("Exito");
			response.setNoActualizadas(noActualizadas);
			response.setTotalRecibidas(datos.size());
			response.setTotalActualizadas(datos.size() - noActualizadas.size());
			response.setData(noAct);
			
		} catch (Exception e) {
			log.error("error", e);
			response.setCod(1);
			response.setSms("Error al acualizar " + e);
		}

		return response;

		
	}
	
	@Override
	public List<catOltsEntidad> obtenerOLTsActivas() {
		List<catOltsEntidad> response = new ArrayList<catOltsEntidad>();
		try {
			response = catalogoOlts.findCatOltsByEstatus(1);

		} catch (Exception e) {
			log.error("error", e);
		}
		return response;
	}

	@Override
	public responseFindOntDto getOnt(String tipo, String data, boolean serie) {
		responseFindOntDto response = new responseFindOntDto();
		catOltsEntidad oltAllData = null;
		Integer idRegion = 0;
		Integer idOlt = 0;
		try {

			List<inventarioOntsEntidad> ontAllData = null;
			switch (tipo) {
				case "T":
					if (serie) {
						ontAllData = inventarioOnts.findOntBySerieT(data);
					} else {
						ontAllData = inventarioOnts.findOntByAliasT(data);
					}
				break;
				case "E":
					if (serie) {
						ontAllData = inventarioOnts.findOntBySerieE(data);
					} else {
						ontAllData = inventarioOnts.findOntByAliasE(data);
					}
				break;
				case "V":
					if (serie) {
						ontAllData = inventarioOnts.findOntBySerieV(data);
					} else {
						ontAllData = inventarioOnts.findOntByAliasV(data);
					}
				break;
			}

			if (!ontAllData.isEmpty()) {
				data = ontAllData.get(0).getNumero_serie();
				idRegion = ontAllData.get(0).getId_region();
				idOlt = ontAllData.get(0).getId_olt();
				oltAllData = catalogoOlts.findOltByIdolt(idOlt);

				try {
					totalesOltsDto totales = monitoreo.getTotalesByOlt(idOlt, tipo);
					response.setTotales(totales);
				} catch (Exception e) {
					response.setMessage("No se pudo obtener los totales" + e);
				}

				response.setSuccess(true);
				response.setPage((0));
				response.setMessage("Proceso exitoso.");
				response.setNumeroSerie(data);
				response.setIdRegion(idRegion);
				response.setIdOlt(idOlt);
				response.setListOnts(ontAllData);
				response.setNombre(oltAllData.getNombre());
				response.setIp(oltAllData.getIp());

			} else {
				response.setMessage("No se pudo localizar la ont");
			}
		} catch (Exception e) {
			response.setMessage("No se pudo localizar la ont");
			log.error("error", e);
		}

		return response;
	}

	@Override
	public responseMetricasDto getMetrics(Integer idOlt, String oid) {
		
		
		responseMetricasDto response = new responseMetricasDto();
		
		try {
			String idEjecucion  = monitor.getLastId().get_id(); // "640774190ba5db75b4cb89d3";//
			Date fechaEjecucion= monitor.getLastId().getFecha_fin();
				
			
			poleosLastUpTimeEntidad poleoLastUpTimeV = poleoLastUpTime.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosUpBytesEntidad poleoUpBytesV = poleoUpBytes.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosDownBytesEntidad poleoDownBytesV = poleoDownBytes.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosTimeOutEntidad poleoTimeOutV = poleoTimeOut.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosUpPacketsEntidad poleoUpPacketsV = poleoUpPackets.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosDownPacketsEntidad poleoDownPacketsV = poleoDownPackets.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosDropUpPacketsEntidad poleoDropUpPacketsV = poleoDropUpPackets.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosDropDownPacketsEntidad poleoDropDownPacketsV = poleoDropDownPackets.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosCpuEntidad poleoCpuV = poleoCpu.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosMemoryEntidad poleoMemoryV = poleoMemory.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
			poleosProfNameEntidad poleoProfNameV = poleoProfName.getMetricaByIndex(idEjecucion, idOlt+"-"+oid);
	
			response.setFechaPoleo(fechaEjecucion);
			response.setLastUpTime(poleoLastUpTimeV  !=  null ? poleoLastUpTimeV.getValor():"");
			response.setUpBytes(poleoUpBytesV  !=  null ? poleoUpBytesV.getValor():"");
			response.setDownBytes(poleoDownBytesV  !=  null ? poleoDownBytesV.getValor():"");
			response.setTimeOut(poleoTimeOutV  !=  null ? poleoTimeOutV.getValor():"");
			response.setUpPackets(poleoUpPacketsV  !=  null ? poleoUpPacketsV.getValor():"");
			response.setDownPackets(poleoDownPacketsV  !=  null ? poleoDownPacketsV.getValor():"");
			response.setDropUpPackets(poleoDropUpPacketsV  !=  null ? poleoDropUpPacketsV.getValor():"");
			response.setDropDownPackets(poleoDropDownPacketsV  !=  null ? poleoDropDownPacketsV.getValor():"");
			response.setCpu(poleoCpuV  !=  null ? poleoCpuV.getValor():"");
			response.setMemoria(poleoMemoryV  !=  null ? poleoMemoryV.getValor():"");
			response.setProfName(poleoProfNameV  !=  null ? poleoProfNameV.getValor():"");
			
			
			
		}catch(Exception e) {
			log.error("error", e);
			return null;
		
		}
		
		return response;
	}

	@Override
	public List<String> getArchivo(Integer arc, String usuario) {
		List<String> archivo =new ArrayList<>();
		
		try {
			BufferedReader lector = new BufferedReader(new FileReader(arc==10 ? rutaNCE+usuario+".txt": arc==1?rutaDescubrimiento:rutaMetrica));
			String linea = lector.readLine();
			while (linea != null) {
				archivo.add(linea);
			   linea = lector.readLine();
			   
			}
		} catch (Exception e) {
			log.error("error", e);
		}

	return archivo;
	}

	@Override
	public responseDto actualizaOnt(String serie,Integer idOlt) {
		responseDto response= new responseDto();
		inventarioOntsEntidad ont =new inventarioOntsEntidad();
		inventarioOntsEntidad onts =new inventarioOntsEntidad();
		try {
			response.setCod(0);
			response.setSms("Se asigno correctamente la ont");
			ont=inventarioOnts.getOntBySerie(serie);
			if(ont==null){
				DiferenciasManualEntity diferencia=	diferencias.getOntBySerieOlt(idOlt,serie);
				BeanUtils.copyProperties(onts, diferencia);
				inventarioOnts.save(onts);
				
			}else{
				DiferenciasManualEntity diferencia=	diferencias.getOntBySerieOlt(idOlt,serie);
				ont.setEstatus(diferencia.getEstatus());
				ont.setIndex(diferencia.getIndex());
				ont.setId_olt(diferencia.getId_olt());
				ont.setIndexFSP(diferencia.getIndexFSP());
				ont.setId_puerto(diferencia.getId_puerto());
				ont.setTecnologia(diferencia.getTecnologia());
				ont.setId_region(diferencia.getId_region());
				ont.setOid(diferencia.getOid());
				inventarioOnts.save(ont);
			}
			List<DiferenciasManualEntity> supDife=	diferencias.getOntBySerie(serie);
				for(DiferenciasManualEntity d:supDife){
					diferencias.deleteById(d.get_id());
				}

		} catch (Exception e) {
			response.setCod(1);
			response.setSms("Error al actualizar la Ont "+ e);
			log.error("error", e);
		}
		return response;
	}

	@Override
	public List<DetalleActualizacionesOltsPojo> getDetalleActualizacionOlt() throws InvocationTargetException, IllegalAccessException {
		List<DetalleActualizacionesOltsPojo> list = new ArrayList<>();
		DetalleActualizacionesOltsPojo obj = null;

		for (DetalleActualizacionesOltsEntity det : detalleActualizacionesOltsRepository.findAll() ){
			obj = new DetalleActualizacionesOltsPojo();
			BeanUtils.copyProperties(obj,det);
			list.add(obj);
		}

		return list;
	}

}
