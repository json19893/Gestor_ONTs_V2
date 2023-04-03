package totalplay.monitor.snmp.com.negocio.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.monitor.snmp.com.negocio.dto.requestEstatusDto;
import totalplay.monitor.snmp.com.negocio.dto.responseFindDto;
import totalplay.monitor.snmp.com.negocio.dto.responseFindOntDto;
import totalplay.monitor.snmp.com.negocio.dto.responseMetricasDto;
import totalplay.monitor.snmp.com.negocio.dto.responseRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.respuestaCambioEstatusDto;
import totalplay.monitor.snmp.com.negocio.dto.respuestaStatusDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOltsDto;
import totalplay.monitor.snmp.com.negocio.service.IconsultaService;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.util.utils;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.detalleActualizacionesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsPdmEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitorPoleoEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosCpuEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosDownBytesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosDownPacketsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosDropDownPacketsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosDropUpPacketsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosLastUpTimeEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosMemoryEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosProfNameEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosTimeOutEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosUpBytesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosUpPacketsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEmpresarialesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IdetalleActualizacionRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOltsReposirorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsPdmRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.ImonitorPoleoRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoCpuRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoDownBytesRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoDownPacketsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoDropDownPacketsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoDropUpPacketsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoLastUpTimeRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoMemoryRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoProfNameRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoTimeOutRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoUpBytesRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IpoleoUpPacketsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsEmpresarialesRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IvwTotalOntsRepositorio;

@Service
public class consultaServiceImpl extends utils implements IconsultaService {

	@Autowired
	IcatOltsRepositorio catalogoOlts;
	@Autowired
	IinventarioOntsRepositorio inventarioOnts;
	@Autowired
	IvwTotalOntsRepositorio vwOnts;
	@Autowired
	IinventarioOltsReposirorio invOLts;
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
			response.put("message", "Error al conseguir en nùmero de serie");
		}
		// System.out.println(olt.getId_olt());
		// System.out.println(ont.getNumero_serie());
		return response;
	}

	@Override
	public responseFindDto getOlt(String tipo, String data, boolean ip) {
		responseFindDto response = new responseFindDto();
		responseRegionDto responseMonitoreo = new responseRegionDto();
		List<vwTotalOntsEmpresarialesEntidad> oltsEmp = new ArrayList<vwTotalOntsEmpresarialesEntidad>();
		List<vwTotalOntsEntidad> olts = new ArrayList<vwTotalOntsEntidad>();
		Integer idRegion = 0;

		switch (tipo) {
		case "T":
			if (ip) {
				olts = vwOnts.findByIp(data);
			} else {
				olts = vwOnts.findByNombre(data);
			}
			if (olts.isEmpty()) {
				response.setMessage("No se pudo localizar la olt");
				return response;
			}
			break;
		case "E":
			if (ip) {
				oltsEmp = vwOntsEmp.findByIp(data);
			} else {
				oltsEmp = vwOntsEmp.findByNombre(data);
			}
			if (oltsEmp.isEmpty()) {
				response.setMessage("No se pudo localizar la olt");
				return response;
			}
			break;
		}

		idRegion = tipo.compareTo("T") == 0 ? olts.get(0).getId_region() : oltsEmp.get(0).getId_region();

		try {
			responseMonitoreo = monitoreo.getOltsByRegion(idRegion, tipo, true);
			responseMonitoreo.setTotalesRegion(olts);
			responseMonitoreo.setTotalesRegionEmp(oltsEmp);
		} catch (Exception e) {
			response.setMessage("Error al cargar los datos de la base " + e);
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
						
						
						res.setFecha_descubrimiento(LocalDateTime.now().toString());
						res.setActualizacion(6);
						res.setTipo(d.getTipo());
						
						if(olt != null && olt.getId_olt().intValue() == res.getid_olt().intValue()) {
							res.setid_olt(olt.getId_olt());
							res.setFrame(d.getFrame());
							res.setSlot(d.getSlot());
							res.setPort(d.getPort());
							res.setId_puerto(d.getOid());
							res.setUid(d.getUid());
							res.setOid(d.getOid()+d.getUid());
						}
						
						invOLts.save(res);
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
						
						if(olt !=null &&  olt.getId_olt().intValue() == resPdm.getid_olt().intValue()) {
							resPdm.setid_olt(olt.getId_olt());
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
			System.out.println("Error a actualizar estatus: " + e);
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
			
			
			/*response.setMessage("Exito");
			response.setSuccess(true);
			response.setEstatusONTs(respuestaONT);*/

		} catch (Exception e) {
			/*System.out.println("Error a actualizar estatus: " + e);
			response.setSuccess(false);
			response.setMessage("Error al acualizar estatus");*/
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
			}

			if (!ontAllData.isEmpty()) {
				data = ontAllData.get(0).getNumero_serie();
				idRegion = ontAllData.get(0).getId_region();
				idOlt = ontAllData.get(0).getid_olt();
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
		}

		return response;
	}

	@Override
	public responseMetricasDto getMetrics(Integer idOlt, String oid) {
		
		
		responseMetricasDto response = new responseMetricasDto();
		
		try {
			String idEjecucion  = monitor.getLastId().get_id();
			
			
			
			//(poleo.*.getMetrica\(idEjecucion, idOlt, oid\))(\.getValor\(\))
			poleosLastUpTimeEntidad poleo =  poleoLastUpTime.getMetrica(idEjecucion, idOlt, oid);
			
			 System.out.println(idEjecucion);
			poleosLastUpTimeEntidad poleoLastUpTimeV = poleoLastUpTime.getMetrica(idEjecucion, idOlt, oid);
			poleosUpBytesEntidad poleoUpBytesV = poleoUpBytes.getMetrica(idEjecucion, idOlt, oid);
			poleosDownBytesEntidad poleoDownBytesV = poleoDownBytes.getMetrica(idEjecucion, idOlt, oid);
			poleosTimeOutEntidad poleoTimeOutV = poleoTimeOut.getMetrica(idEjecucion, idOlt, oid);
			poleosUpPacketsEntidad poleoUpPacketsV = poleoUpPackets.getMetrica(idEjecucion, idOlt, oid);
			poleosDownPacketsEntidad poleoDownPacketsV = poleoDownPackets.getMetrica(idEjecucion, idOlt, oid);
			poleosDropUpPacketsEntidad poleoDropUpPacketsV = poleoDropUpPackets.getMetrica(idEjecucion, idOlt, oid);
			poleosDropDownPacketsEntidad poleoDropDownPacketsV = poleoDropDownPackets.getMetrica(idEjecucion, idOlt, oid);
			poleosCpuEntidad poleoCpuV = poleoCpu.getMetrica(idEjecucion, idOlt, oid);
			poleosMemoryEntidad poleoMemoryV = poleoMemory.getMetrica(idEjecucion, idOlt, oid);
			poleosProfNameEntidad poleoProfNameV = poleoProfName.getMetrica(idEjecucion, idOlt, oid);
			
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
			return null;
		}
		
		return response;
	}

}
