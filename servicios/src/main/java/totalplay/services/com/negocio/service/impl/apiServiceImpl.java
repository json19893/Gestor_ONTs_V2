package totalplay.services.com.negocio.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.services.com.negocio.dto.datosNumeroSerieDto;
import totalplay.services.com.negocio.dto.ejecucionDto;
import totalplay.services.com.negocio.dto.requestAltaOnts;
import totalplay.services.com.negocio.dto.requestCambioOltDto;
import totalplay.services.com.negocio.dto.requestDto;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.dto.requestEstatusOltDto;
import totalplay.services.com.negocio.dto.responseDto;
import totalplay.services.com.negocio.dto.responseEstatusOltDto;
import totalplay.services.com.negocio.dto.respuestaDto;
import totalplay.services.com.negocio.dto.respuestaStatusDto;
import totalplay.services.com.negocio.service.IapiService;
import totalplay.services.com.negocio.utils.GetToken;
import totalplay.services.com.negocio.utils.util;
import totalplay.services.com.persistencia.entidad.InventarioOntsRespNCEEntity;
import totalplay.services.com.persistencia.entidad.catOltsEntidad;
import totalplay.services.com.persistencia.entidad.catOltsInventarioEntidad;
import totalplay.services.com.persistencia.entidad.catOtsProcesadoEntidad;
import totalplay.services.com.persistencia.entidad.detalleActualizacionesEntidad;
import totalplay.services.com.persistencia.entidad.detalleActualizacionesOltEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsTempEntidad;
import totalplay.services.com.persistencia.entidad.oltsNcePolearEntidad;
import totalplay.services.com.persistencia.repositorio.IDetalleActualizacionesOltRepositorio;
import totalplay.services.com.persistencia.repositorio.IcatOltsInventarioRepositorio;
import totalplay.services.com.persistencia.repositorio.IcatOltsProcesadoRepositorio;
import totalplay.services.com.persistencia.repositorio.IcatOltsRepositorio;
import totalplay.services.com.persistencia.repositorio.IdetalleActualizacionRepositorio;
import totalplay.services.com.persistencia.repositorio.IinventarioOntsRepositorio;
import totalplay.services.com.persistencia.repositorio.IinventarioOntsRespNCERepository;
import totalplay.services.com.persistencia.repositorio.IinventarioOntsTempRepositorio;
import totalplay.services.com.persistencia.repositorio.InventarioOntRespRepository;
import totalplay.services.com.persistencia.repositorio.IoltsNcePolearRepositorio;

@Service
@Slf4j
public class apiServiceImpl implements IapiService {
	@Autowired
	IinventarioOntsRepositorio onts;
	@Autowired
	IinventarioOntsTempRepositorio onts2;
	@Autowired
	InventarioOntRespRepository ontsInventarioRespaldoRepository;
	@Autowired
	IcatOltsProcesadoRepositorio status;
	@Autowired
	IcatOltsRepositorio catalogoOlts;
	@Autowired
	IdetalleActualizacionRepositorio detalleRepositorio;
	@Autowired
	IinventarioOntsRespNCERepository ontsResp;
	@Autowired
	IoltsNcePolearRepositorio ncePolearRepositorio;
	@Autowired
	IDetalleActualizacionesOltRepositorio detalleActualizacionesOlt;
	@Autowired
	IcatOltsInventarioRepositorio catalogoOltsInventario;

	@Value("${ruta.archivo.shell}")
	private String ruta;
	util util = new util();

	@Override
	public respuestaDto getNumeroSerie(requestDto datos) throws Exception {
		respuestaDto response = new respuestaDto();
		try {

			if (datos.getFrame().equals("") || datos.getFrame().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el frame");
				return response;

			}
			if (datos.getIp().equals("") || datos.getIp().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar la ip");
				return response;

			}

			if (datos.getPort().equals("") || datos.getPort().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el puerto");
				return response;
			}

			if (datos.getSlot().equals("") || datos.getSlot().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el slot");
				return response;
			}

			if (datos.getUid().equals("") || datos.getUid().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el UID");
				return response;
			}

			catOltsEntidad olt = catalogoOlts.getIp(datos.getIp());
			if (olt == null) {
				response.setCod(1);
				response.setSms("La ip ingresada no existe");
				return response;
			}

			List<inventarioOntsEntidad> res = onts.getNumeroSerie(datos.getFrame(), datos.getPort(), datos.getSlot(),
					datos.getUid(), olt.getId_olt());
			if (res.isEmpty()) {
				response.setCod(1);
				response.setSms("No se encontro información con los criteros de busqueda ingresados");
				return response;
			}
			response.setCod(0);
			response.setSms("Exito");
			List<datosNumeroSerieDto> dataSerie = new ArrayList<datosNumeroSerieDto>();
			for (inventarioOntsEntidad s : res) {
				datosNumeroSerieDto r = new datosNumeroSerieDto();
				r.setNumeroSerie(s.getNumero_serie());
				r.setTipo(s.getTipo());
				r.setEstatus(s.getEstatus() == 1 ? "UP" : "DOWN");
				dataSerie.add(r);
			}
			response.setNumeroSerie(dataSerie);
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("error al procesar la solicitud");
			log.error("ERROR: ", e);
			return response;
		}
		return response;
	}

	@Override
	public respuestaStatusDto putStatusOnt(List<requestEstatusDto> datos) throws Exception {

		respuestaStatusDto response = new respuestaStatusDto();
		List<detalleActualizacionesEntidad> actualizadas = new ArrayList<>();
		List<detalleActualizacionesEntidad> noActualizadas = new ArrayList<>();
		try {
			for (requestEstatusDto d : datos) {

				detalleActualizacionesEntidad na = new detalleActualizacionesEntidad();
				catOltsEntidad olt = catalogoOlts.getIp(d.getIp());
				if (olt == null) {
					na.setCausa("No existe la olt proporcionada");
					na.setNumeroSerie("na");
					na.setIp(d.getIp());
					na.setFrame(d.getFrame());
					na.setSlot(d.getSlot());
					na.setPort(d.getPort());
					na.setDescripcionAlarma(d.getDescripcionAlarma());
					na.setFechaActualizacion(util.getDate());
					na.setUid(d.getUid());
					noActualizadas.add(na);

				} else {
					// Se realiza una busqueda por algunos criterios:
					List<inventarioOntsEntidad> res = onts.getOnt(d.getFrame(), d.getPort(), d.getSlot(), d.getUid(),
							olt.getId_olt());

					// Si esta vacio la lista busca en la tabla de respaldos:
					// List<InventarioOntResp> ontInventarioRespaldo = new ArrayList<>();
					/*
					 * if (res.isEmpty()) { ontInventarioRespaldo = ontsInventarioRespaldoRepository
					 * .getOntsRespaldo(olt.getId_olt(), d.getUid(), d.getFrame(), d.getPort(),
					 * d.getSlot()); }
					 */

					if (res.isEmpty() /* && ontInventarioRespaldo.isEmpty() */) {
						na.setCausa("No existe una ont asociada a los criterios de busqueda");
						na.setNumeroSerie("na");
						na.setIp(d.getIp());
						na.setFrame(d.getFrame());
						na.setSlot(d.getSlot());
						na.setPort(d.getPort());
						na.setDescripcionAlarma(d.getDescripcionAlarma());
						na.setFechaActualizacion(util.getDate());
						na.setUid(d.getUid());
						noActualizadas.add(na);

					} else {
						/*
						 * if (res.isEmpty() && !ontInventarioRespaldo.isEmpty()) {
						 * 
						 * inventarioOntsEntidad tmp; res = new ArrayList<>(); for (InventarioOntResp
						 * ontResp : ontInventarioRespaldo) { tmp = new inventarioOntsEntidad();
						 * tmp.set_id(ontResp.get_id()); tmp.setNumero_serie(ontResp.getNumero_serie());
						 * tmp.setOid(ontResp.getOid()); tmp.setFecha_descubrimiento(util.getDate());
						 * tmp.setId_olt(ontResp.getId_olt()); tmp.setEstatus(ontResp.getEstatus());
						 * tmp.setId_ejecucion(ontResp.getId_ejecucion());
						 * tmp.setAlias(ontResp.getAlias()); tmp.setId_region(ontResp.getId_region());
						 * tmp.setFrame(ontResp.getFrame()); tmp.setSlot(ontResp.getSlot());
						 * tmp.setPort(ontResp.getPort()); tmp.setUid(ontResp.getUid());
						 * tmp.setTipo(ontResp.getTipo());
						 * tmp.setDescripcionAlarma(ontResp.getDescripcionAlarma());
						 * tmp.setTecnologia(ontResp.getTecnologia());
						 * tmp.setLastDownTime(ontResp.getLastDownTime());
						 * tmp.setActualizacion(ontResp.getActualizacion());
						 * tmp.setVip(ontResp.getVip()); res.add(tmp); } }
						 */

						for (inventarioOntsEntidad r : res) {
							if (d.getEstatus().equals("UP") || d.getEstatus().equals("CLEAR")) {
								r.setEstatus(1);
								r.setDescripcionAlarma(d.getDescripcionAlarma());
								r.setFecha_descubrimiento(util.getDate());
								r.setActualizacion(1);
								na.setCausa("Actualizacion a UP");
								na.setNumeroSerie(r.getNumero_serie());
								na.setIp(d.getIp());
								na.setFrame(d.getFrame());
								na.setSlot(d.getSlot());
								na.setPort(d.getPort());
								na.setDescripcionAlarma(d.getDescripcionAlarma());
								na.setFechaActualizacion(util.getDate());
								na.setUid(d.getUid());
								actualizadas.add(na);

							} else {
								r.setEstatus(2);
								r.setDescripcionAlarma(d.getDescripcionAlarma());
								r.setFecha_descubrimiento(util.getDate());
								r.setActualizacion(2);
								na.setCausa("Actualizacion a DOWN");
								na.setNumeroSerie(r.getNumero_serie());
								na.setIp(d.getIp());
								na.setFrame(d.getFrame());
								na.setSlot(d.getSlot());
								na.setPort(d.getPort());
								na.setDescripcionAlarma(d.getDescripcionAlarma());
								na.setFechaActualizacion(util.getDate());
								na.setUid(d.getUid());
								actualizadas.add(na);
							}
							/*
							 * if (onts.save(r) != null) { if
							 * (ontsInventarioRespaldoRepository.findById(r.get_id()).isPresent()) {
							 * ontsInventarioRespaldoRepository.deleteById(r.get_id()); } }
							 */
							onts.save(r);
						}
					}
				}
			}

			detalleRepositorio.saveAll(actualizadas);
			detalleRepositorio.saveAll(noActualizadas);
			response.setCod(0);
			response.setSms("Exito");
			response.setNoActualizadas(noActualizadas);
			response.setTotalRecibidas(datos.size());
			response.setTotalActualizadas(datos.size() - noActualizadas.size());

		} catch (Exception e) {
			log.error("Error a actualizar estatus: " + e);
			response.setCod(1);
			response.setSms("Error al acualizar " + e);
		}

		return response;
	}

	@Override
	public responseEstatusOltDto putStatusOlt(List<requestEstatusOltDto> datos) throws Exception {
		responseEstatusOltDto response = new responseEstatusOltDto();
		List<detalleActualizacionesOltEntidad> actualizadas = new ArrayList<detalleActualizacionesOltEntidad>();
		List<detalleActualizacionesOltEntidad> noActualizadas = new ArrayList<detalleActualizacionesOltEntidad>();
		List<oltsNcePolearEntidad> oltsPo = new ArrayList<oltsNcePolearEntidad>();
		try {
			for (requestEstatusOltDto d : datos) {
				detalleActualizacionesOltEntidad act = new detalleActualizacionesOltEntidad();
				oltsNcePolearEntidad oltsPolear = new oltsNcePolearEntidad();
				catOltsEntidad olt = catalogoOlts.getOltByIpName(d.getIp(), d.getNombre());
				if (olt == null) {
					act.setCausa(d.getCausa());
					act.setDescripcion("No se informacion de la olt ");
					act.setFechaRecibida(d.getFecha());
					act.setIp(d.getIp());
					act.setNombre(d.getNombre());
					act.setStatus(d.getStatus());
					act.setFechaRegistro(util.getDate());
					act.setCorrecta(1);
					noActualizadas.add(act);
				} else {

					if (d.getStatus().equals("UP")) {
						olt.setDescripcion("");
						olt.setEstatus(1);
						catalogoOlts.save(olt);
						oltsPolear.setEstatus_poleo(0);
						oltsPolear.setFecha_registro(util.getDate());
						oltsPolear.setId_olt(olt.getId_olt());
						oltsPolear.setNombre(olt.getNombre());
						oltsPo.add(oltsPolear);

						act.setCausa(d.getCausa());
						act.setDescripcion(d.getDescripcion());
						act.setFechaRecibida(d.getFecha());
						act.setIp(d.getIp());
						act.setNombre(d.getNombre());
						act.setStatus(d.getStatus());
						act.setFechaRegistro(util.getDate());
						act.setCorrecta(0);
						actualizadas.add(act);

					} else if (d.getStatus().equals("DOWN")) {
						olt.setDescripcion(d.getDescripcion() + " -> Causa: " + d.getCausa());
						olt.setEstatus(0);
						List<inventarioOntsTempEntidad> acpd = new ArrayList<>();
						List<inventarioOntsEntidad> acif = new ArrayList<>();
						List<inventarioOntsTempEntidad> ontPdm = onts2.getOntByOlt(olt.getId_olt());
						List<inventarioOntsEntidad> ontFinal = onts.getOntByOlt(olt.getId_olt());
						try {
							onts2.updateOnt(olt.getId_olt());

						} catch (Exception e) {

						}
						try {
							onts.updateOnt(olt.getId_olt());
						} catch (Exception e) {
							// TODO: handle exception
						}

						catalogoOlts.save(olt);
						act.setCausa(d.getCausa());
						act.setDescripcion(d.getDescripcion());
						act.setFechaRecibida(d.getFecha());
						act.setIp(d.getIp());
						act.setNombre(d.getNombre());
						act.setStatus(d.getStatus());
						act.setFechaRegistro(util.getDate());
						act.setCorrecta(0);
						actualizadas.add(act);
					} else {
						act.setCausa(d.getCausa());
						act.setDescripcion("No se informo el estatus  ");
						act.setFechaRecibida(d.getFecha());
						act.setIp(d.getIp());
						act.setNombre(d.getNombre());
						act.setStatus(d.getStatus());
						act.setFechaRegistro(util.getDate());
						act.setCorrecta(1);
						noActualizadas.add(act);
					}

				}

				if (oltsPo != null) {
					ncePolearRepositorio.saveAll(oltsPo);
				}
				if (actualizadas != null) {
					detalleActualizacionesOlt.saveAll(actualizadas);
				}
				if (noActualizadas != null) {
					detalleActualizacionesOlt.saveAll(noActualizadas);
				}
				response.setCod(0);
				response.setSms("OK");
				response.setTotalActualizadas(actualizadas.size());
				response.setTotalRecibidas(datos.size());
				response.setNoActualizadas(noActualizadas);
			}
		} catch (Exception e) {
			log.info("ocurrion un error al al actualizar la olt: " + e);
			response.setCod(1);
			response.setSms("Ocurrio un error al procesar");
		}
		return response;
	}

	@Override
	public responseDto getConfiguracionOlt(String tecnologia) throws Exception {
		try {

			List<catOltsEntidad> olts = catalogoOlts.getConfiguracionOlt();

			ejecucionDto respuesta = null;
			String s;
			for (catOltsEntidad olt : olts) {
				log.info("*************************");
				log.info("olt:: " + olt.getIp());

				String comando4 = "snmpbulkwalk -v3 -l authPriv -u  KIOLAB2022 -a SHA -A AUkey#2021* -x DES -X SECkey#2120* "
						+ olt.getIp() + " .";
				catOtsProcesadoEntidad data = new catOtsProcesadoEntidad();

				respuesta = util.execBash(comando4, ruta);

				if (respuesta.getBuffer() != null) {
					while ((s = respuesta.getBuffer().readLine()) != null) {
						log.info("============= " + s + "=============");
						data.setIp("accesskey372");
						data.setFrase("securitykey372");
						data.setProtocolo("DES");
						data.setValor("si");
						status.save(data);

						log.info("ejecuto primer comando:: " + olt);
					}

				}
				log.info("*************************");
			}

		} catch (Exception e) {
			log.error("ERROR:", e);
		}
		return null;
	}

	public respuestaDto altaOnts(requestAltaOnts datos) throws Exception {
		respuestaDto response = new respuestaDto();
		try {
			if (datos.getNumSerie().equals("") || datos.getNumSerie().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el Numero de Serie");
				return response;
			}

			String resEstatus = "";
			String tecnolgia = "";
			String port = util.isBlankOrNull(datos.getPort()) ? "0" : datos.getPort();
			String frame = util.isBlankOrNull(datos.getFrame()) ? "0" : datos.getFrame();
			String slot = util.isBlankOrNull(datos.getSlot()) ? "0" : datos.getSlot();
			String tipo = util.isBlankOrNull(datos.getTipo()) ? "E" : datos.getTipo();
			// String estatus = util.isBlankOrNull(datos.getEstatus()) ? "0" :
			// datos.getEstatus();
			String nombreOlt = util.isBlankOrNull(datos.getNombreOlt()) ? "" : datos.getNombreOlt();
			int estatus = 2;
			inventarioOntsEntidad res = onts.getONT(datos.getNumSerie());
			InventarioOntsRespNCEEntity resResp = ontsResp.getONT(datos.getNumSerie());
			if (util.isBlankOrNull(datos.getEstatus())) {
				// String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
				// estatus = estatusWS.equals("online") ? 1 : 2;
			} else {
				if (datos.getEstatus().equals("UP")) {
					estatus = 1;
				} else if (datos.getEstatus().equals("DOWN")) {
					estatus = 2;
				} else {

				}
			}
			if (res == null && resResp == null) {
				inventarioOntsTempEntidad res2 = onts2.getONT(datos.getNumSerie());
				if (res2 == null) {
					catOltsEntidad olt = catalogoOlts.getIp(datos.getIp());
					if (olt == null) {
						// Guardado de la nueva olt
						olt = saveOlt(datos.getIp(), nombreOlt);
						tecnolgia = olt.getTecnologia();
					}
					// Seteo de valores y guardado de la ont en pdm
					res2 = saveOntPdm(olt, Integer.parseInt(frame), Integer.parseInt(slot), Integer.parseInt(port),
							datos.getNumSerie(), estatus, tipo);
					resEstatus = res2.getEstatus().toString();
				} else {
					res2.setFecha_descubrimiento(util.getDate());
					res2.setTipo(res2.getTipo() == "E" ? res2.getTipo() : tipo);
					res2.setEstatus(estatus);
					onts2.save(res2);
					resEstatus = res2.getEstatus().toString();
				}
			} else if (resResp == null) {
				res.setTipo(res.getTipo() == "E" ? res.getTipo() : tipo);
				res.setActualizacion(3);
				res.setFecha_modificacion(util.getDate());
				// res.setFecha_descubrimiento(LocalDateTime.now().toString());
				onts.save(res);
				resEstatus = res.getEstatus().toString();
			} else {

				inventarioOntsEntidad ontAux = new inventarioOntsEntidad();
				BeanUtils.copyProperties(ontAux, resResp);

				ontAux.setTipo(resResp.getTipo() == "E" ? resResp.getTipo() : tipo);
				ontAux.setActualizacion(3);
				ontAux.setFecha_modificacion(util.getDate());
				ontAux.set_id(null);

				ontsResp.delete(resResp);
				onts.save(ontAux);

				resEstatus = ontAux.getEstatus().toString();
			}
			response.setCod(0);
			response.setSms("Exito");
			List<datosNumeroSerieDto> dataSerie = new ArrayList<datosNumeroSerieDto>();
			datosNumeroSerieDto r = new datosNumeroSerieDto();
			r.setNumeroSerie(datos.getNumSerie());
			r.setTipo("E");
			r.setEstatus(resEstatus);
			dataSerie.add(r);
			response.setNumeroSerie(dataSerie);
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("error al procesar la solicitud");
			log.error("ERROR:", e);
			return response;
		}
		return response;
	}

	public boolean pruebaOLT(String ip, Integer config) {

		try {
			List<catOltsEntidad> olts = catalogoOlts.getConfiguracionOlt();
			// String [] olts= {"10.180.230.12"};
			ejecucionDto respuesta = null;
			String s;
			log.info("*************************");
			log.info("olt:: " + ip);
			String comandoZTE1 = "snmpbulkwalk  -r 1 -t 1 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x DES -X securitykey372 "
					+ ip + " 1.3.6.1.4.1.3902.1012.3.28.1.1.5";
			String comandoZTE2 = "snmpbulkwalk  -r 1 -t 1 -v3 -u ITSMon03 -l authPriv -a SHA -A au*MGTm0n1t0r%03 -x DES -X sc#MGTm0n1t0r$30 "
					+ ip + " 1.3.6.1.4.1.3902.1012.3.28.1.1.5";

			String comandoH1 = "snmpbulkwalk  -r 1 -t 1 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x AES -X sc#MGTm0n1t0r$30 "
					+ ip + " hwGponDeviceOntSn";
			String comandoH2 = "snmpbulkwalk  -r 1 -t 1 -v3 -u ITSMon03 -l authPriv -a SHA -A au*MGTm0n1t0r%03 -x AES -X securitykey372 "
					+ ip + " hwGponDeviceOntSn";

			catOtsProcesadoEntidad data = new catOtsProcesadoEntidad();
			if (config == 1) {
				respuesta = util.execBash(comandoH1, ruta);
			} else if (config == 2) {
				respuesta = util.execBash(comandoH2, ruta);
			} else if (config == 3) {
				respuesta = util.execBash(comandoZTE1, ruta);
			} else if (config == 4) {
				respuesta = util.execBash(comandoZTE2, ruta);
			}

			if (respuesta.getBuffer() != null) {
				while ((s = respuesta.getBuffer().readLine()) != null) {
					log.info("============= " + s + "=============");
					String[] parts = s.split(":");
					if (parts[0].equals("HUAWEI-XPON-MIB")) {
						return true;
					} else if (parts[0].equals("SNMPv2-SMI")) {
						return true;
					} else {
						return false;
					}
				}
				respuesta.getProceso().waitFor(10, TimeUnit.SECONDS);
			}

		} catch (Exception e) {
			log.error("ERROR:", e);
		}
		return false;
	}

	@Override
	public respuestaDto validaONT(requestAltaOnts datos) throws Exception {
		respuestaDto response = new respuestaDto();
		try {
			if (util.isBlankOrNull(datos.getNumSerie()) || util.isBlankOrNull(datos.getIp())) {
				response.setCod(1);
				response.setSms("IP y Numero de Serie son obligatorios");
				return response;
			}
			int estatus = 2;
			inventarioOntsEntidad res = onts.getONT(datos.getNumSerie());
			if (util.isBlankOrNull(datos.getEstatus())) {
				String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
				estatus = estatusWS.equals("online") ? 1 : 2;
			} else {
				if (datos.getEstatus().equals("UP")) {
					estatus = 1;
				} else if (datos.getEstatus().equals("DOWN")) {
					estatus = 2;
				} else {
					String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
					if (util.isBlankOrNull(estatusWS)) {
						estatus = 0;
					} else if (estatusWS.equals("online")) {
						estatus = 1;
					} else if (estatusWS.equals("ONT_no_existe")) {
						estatus = 3;
					} else {
						estatus = 2;
					}
				}
			}
			if (res == null) {
				inventarioOntsTempEntidad res2 = onts2.getONT(datos.getNumSerie());

				res2.setEstatus(estatus);
				onts2.save(res2);
			} else {
				res.setEstatus(estatus);
				onts.save(res);
			}
			response.setCod(0);
			response.setSms("Exito");
			List<datosNumeroSerieDto> dataSerie = new ArrayList<datosNumeroSerieDto>();
			datosNumeroSerieDto r = new datosNumeroSerieDto();
			r.setNumeroSerie(datos.getNumSerie());
			r.setTipo("E");
			dataSerie.add(r);
			response.setNumeroSerie(dataSerie);
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("error al procesar la solicitud");
			log.error("ERROR:", e);
			return response;
		}
		return response;
	}

	public String pruebaONT(String numSerie, String ip) {
		String estatusWS = "";
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			String token;
			GetToken getToken = new GetToken();
			token = getToken.getToken();
			HttpPost request = new HttpPost(
					"http://10.180.199.203:8080/modelos/wizard/241?device=10.180.199.200&sh=1&port=31722&amb=pro&tkn="
							+ token);
			JSONObject params2 = new JSONObject("{\r\n" + "  \"ips\": [\r\n" + "        {\r\n"
					+ "            \"ipOLT\": \"" + ip + "\",\r\n" + "            \"onts\": [\r\n"
					+ "                {\r\n" + "                    \"ns\": \"" + numSerie + "\"\r\n"
					+ "                }\r\n" + "            ]\r\n" + "        }\r\n" + "    ]\r\n" + "}");
			StringEntity params;
			params = new StringEntity(params2.toString());
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			HttpResponse responseHttp = httpClient.execute(request);
			HttpEntity entity2 = responseHttp.getEntity();
			String result2 = EntityUtils.toString(entity2);
			JSONObject jsonObject = new JSONObject(result2);
			JSONObject jsonObject2 = (JSONObject) jsonObject.getJSONArray("ips").get(0);
			JSONObject jsonObjectONT = (JSONObject) jsonObject2.getJSONArray("onts").get(0);
			estatusWS = jsonObjectONT.getString("status");
		} catch (Exception e) {
		}
		return estatusWS;
	}

	private catOltsEntidad saveOlt(String ip, String nombreOlt) {

		String tecnologia = "";
		Integer idOlt = catalogoOlts.getIdOltMAX().getId_olt() + 1;
		catOltsEntidad olt1 = new catOltsEntidad();
		if (pruebaOLT(ip, 1)) {
			olt1.setId_configuracion(1);
			tecnologia = "HUAWEI";
		} else if (pruebaOLT(ip, 2)) {
			olt1.setId_configuracion(2);
			tecnologia = "HUAWEI";
		} else if (pruebaOLT(ip, 3)) {
			olt1.setId_configuracion(3);
			tecnologia = "ZTE";
		} else if (pruebaOLT(ip, 4)) {
			olt1.setId_configuracion(4);
			tecnologia = "ZTE";
		}
		olt1.setId_region(11);
		olt1.setId_olt(idOlt);
		olt1.setTecnologia(tecnologia);
		olt1.setNombre(nombreOlt);
		olt1.setIp(ip);
		olt1.setEstatus(1);
		catalogoOlts.save(olt1);
		return olt1;

	}

	private inventarioOntsTempEntidad saveOntPdm(catOltsEntidad olt, Integer frame, Integer slot, Integer port,
			String serie, Integer estatus, String tipo) {

		inventarioOntsTempEntidad res2 = new inventarioOntsTempEntidad();

		res2.setId_region(olt.getId_region());
		res2.setNumero_serie(serie);
		res2.setId_olt(olt.getId_olt());
		res2.setTipo(tipo);
		res2.setEstatus(estatus);
		res2.setSlot(slot);
		res2.setPort(port);
		res2.setFrame(frame);
		res2.setDescripcionAlarma("Estado Inicial");
		res2.setTecnologia(olt.getTecnologia());
		res2.setFecha_descubrimiento(util.getDate());
		res2.setLastDownTime("---");
		onts2.save(res2);

		return res2;
	}

	@Override
	public respuestaStatusDto cambioIPOlt(requestCambioOltDto datos) throws Exception {
		respuestaStatusDto response = new respuestaStatusDto();
		try {
			catOltsEntidad olt = catalogoOlts.getIp(datos.getIpAnterior());
			if (olt != null) {
				catOltsEntidad oltNueva = catalogoOlts.getIp(datos.getIpNueva());
				if (oltNueva == null) {
					catOltsInventarioEntidad oltAnterior = new catOltsInventarioEntidad();
					oltAnterior.set_id(olt.get_id());
					oltAnterior.setDescripcion(olt.getDescripcion());
					oltAnterior.setIp(olt.getIp());
					oltAnterior.setNombre(olt.getNombre());
					oltAnterior.setTecnologia(olt.getTecnologia());
					oltAnterior.setId_region(olt.getId_region());
					catalogoOltsInventario.save(oltAnterior);
					olt.setIp(datos.getIpNueva());
					catalogoOlts.save(olt);
				} else {
					List<inventarioOntsEntidad> res = onts.getOntByOlt(oltNueva.getId_olt());
					catOltsInventarioEntidad oltAnterior = new catOltsInventarioEntidad();
					oltAnterior.set_id(olt.get_id());
					oltAnterior.setDescripcion(olt.getDescripcion());
					oltAnterior.setIp(olt.getIp());
					oltAnterior.setNombre(olt.getNombre());
					oltAnterior.setTecnologia(olt.getTecnologia());
					oltAnterior.setId_region(olt.getId_region());
					catalogoOltsInventario.save(oltAnterior);
					oltNueva.setId_region(olt.getId_region());
					oltNueva.setId_configuracion(olt.getId_configuracion());
					oltNueva.setTecnologia(olt.getTecnologia());
					oltNueva.setEstatus(1);
					catalogoOlts.save(oltNueva);
					catalogoOlts.delete(olt);
					if (!res.isEmpty()) {
						for (inventarioOntsEntidad r : res) {
							r.setId_olt(oltNueva.getId_olt());
							r.setIndex(oltNueva.getId_olt()+r.getOid());
							r.setIndexFSP(oltNueva.getId_olt()+r.getId_puerto());
							onts.save(r);
						}
					}
				}
			}
			response.setCod(0);
			response.setSms("Exito");

		} catch (Exception e) {
			log.info("Error a actualizar estatus: " + e);
			response.setCod(1);
			response.setSms("Error al acualizar " + e);
		}

		return response;
	}

}