package totalplay.services.com.negocio.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.services.com.negocio.dto.datosNumeroSerieDto;
import totalplay.services.com.negocio.dto.datosOntsDto;
import totalplay.services.com.negocio.dto.ejecucionDto;
import totalplay.services.com.negocio.dto.requestAltaOnts;
import totalplay.services.com.negocio.dto.requestCambioOlt;
import totalplay.services.com.negocio.dto.requestDto;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.dto.responseDto;
import totalplay.services.com.negocio.dto.respuestaDto;
import totalplay.services.com.negocio.dto.respuestaStatusDto;
import totalplay.services.com.negocio.service.IapiService;
import totalplay.services.com.negocio.utils.GetToken;
import totalplay.services.com.negocio.utils.util;
import totalplay.services.com.persistencia.entidad.catOltsEntidad;
import totalplay.services.com.persistencia.entidad.catOltsInventarioEntidad;
import totalplay.services.com.persistencia.entidad.catOtsProcesadoEntidad;
import totalplay.services.com.persistencia.entidad.detalleActualizacionesEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsTempEntidad;
import totalplay.services.com.persistencia.repositorio.IcatOltsInventarioRepositorio;
import totalplay.services.com.persistencia.repositorio.IcatOltsProcesadoRepositorio;
import totalplay.services.com.persistencia.repositorio.IcatOltsRepositorio;
import totalplay.services.com.persistencia.repositorio.IdetalleActualizacionRepositorio;
import totalplay.services.com.persistencia.repositorio.IinventarioOntsRepositorio;
import totalplay.services.com.persistencia.repositorio.IinventarioOntsTempRepositorio;


@Service
public class apiServiceImpl implements IapiService {
	@Autowired
	IinventarioOntsRepositorio onts;
	@Autowired
	IinventarioOntsTempRepositorio onts2;
	@Autowired
	IcatOltsProcesadoRepositorio status;
	@Autowired
	IcatOltsRepositorio catalogoOlts;
	@Autowired
	IcatOltsInventarioRepositorio catalogoOltsInventario;
	@Autowired
	IdetalleActualizacionRepositorio detalleRepositorio;
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
				dataSerie.add(r);
			}
			response.setNumeroSerie(dataSerie);
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("error al procesar la solicitud");
			return response;
		}
		return response;
	}
	
	@Override
	public respuestaStatusDto cambioIPOlt(requestCambioOlt datos) throws Exception {
		respuestaStatusDto response = new respuestaStatusDto();
		List<detalleActualizacionesEntidad> actualizadas = new ArrayList<detalleActualizacionesEntidad>();
		List<detalleActualizacionesEntidad> noActualizadas = new ArrayList<detalleActualizacionesEntidad>();
		try {
			//for (requestCambioOlt d : datos) {
				detalleActualizacionesEntidad na = new detalleActualizacionesEntidad();
				catOltsEntidad olt = catalogoOlts.getIp(datos.getIpAnterior());
				
				if (olt == null) {
					
				} else {
					catOltsEntidad oltNueva = catalogoOlts.getIp(datos.getIpNueva());
					if(oltNueva == null) {
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
						List<inventarioOntsEntidad> res = onts.getONTbyOlt(oltNueva.getId_olt());
						catOltsInventarioEntidad oltAnterior = new catOltsInventarioEntidad();
						oltAnterior.set_id(olt.get_id());
						oltAnterior.setDescripcion(olt.getDescripcion());
						oltAnterior.setIp(olt.getIp());
						oltAnterior.setNombre(olt.getNombre());
						oltAnterior.setTecnologia(olt.getTecnologia());
						oltAnterior.setId_region(olt.getId_region());
						catalogoOltsInventario.save(oltAnterior);
						catalogoOlts.delete(oltNueva);
						if (!res.isEmpty()) {
							for (inventarioOntsEntidad r : res) {
								r.setId_olts(oltNueva.getId_olt());
								onts.save(r);
							}
						} else {
							
						}
					}
				}
			//}
			detalleRepositorio.saveAll(actualizadas);
			response.setCod(0);
			response.setSms("Exito");
			response.setNoActualizadas(noActualizadas);
			//response.setTotalRecibidas(datos.size());
			//response.setTotalActualizadas(datos.size() - noActualizadas.size());

		} catch (Exception e) {
			System.out.println("Error a actualizar estatus: " + e);
			response.setCod(1);
			response.setSms("Error al acualizar " + e);
		}

		return response;
	}
	
	
	@Override
	public responseDto getConfiguracionOlt(String tecnologia) throws Exception {
		try {
			System.out.println("tecnologia:: "+tecnologia);
			List<catOltsEntidad> olts = catalogoOlts.getConfiguracionOlt();
			 //String [] olts= {"10.180.230.12"};
			ejecucionDto respuesta=null;
			String s;
			for(catOltsEntidad olt:olts) {
				System.out.println("*************************");
				System.out.println("olt:: "+olt.getIp());
				/*String comando1 = "snmpget -v3 -l authPriv -u  userAGPON17 -a SHA -A accesskey372 -x AES -X securitykey372  " + olt
						+ " .";
				String comando2 = "snmpbulkwalk -v3 -l authPriv -u  ITSMon03 -a SHA -A au*MGTm0n1t0r%03 -x AES -X sc#MGTm0n1t0r$30 " + olt
				+ " hwGponDeviceOntSn";
				
				String comando3 = "snmpbulkwalk -v3 -l authPriv -u  ITSMon03 -a SHA -A au\\\\*MGTm0n1t0r\\\\%03 -x AES -X sc\\\\#MGTm0n1t0r\\\\$30 " + olt
						+ " .";*/
				
				String comando4 = "snmpbulkwalk -v3 -l authPriv -u  KIOLAB2022 -a SHA -A AUkey#2021* -x DES -X SECkey#2120* " + olt.getIp()
				+ " .";
				catOtsProcesadoEntidad data=new catOtsProcesadoEntidad();
				/*data.setId_olt(olt.getId_olt());
				data.setId_region(olt.getId_region());
				data.setIp(olt.getIp());
				data.setNombre(olt.getNombre());
				data.setTecnologia(olt.getTecnologia());*/
				//catOltsEntidad cat=	 catalogoOlts.getOlt(olt.getId_olt());
				respuesta=util.ejecutaComando(comando4);
				
				 if(respuesta.getBuffer()!=null) {
						while ((s = respuesta.getBuffer().readLine()) != null) {
							System.out.println("============= "+s+"=============");
							 data.setPassword("accesskey372");
							 data.setFrase("securitykey372");
							 data.setProtocolo("DES");
							 data.setValor("si");
							 //cat.setEstatus(1);
							 //cat.setId_configuracion(1);
							 //cat.setTecnologia("HUAWEI");
							 //catalogoOlts.save(cat);
							 status.save(data);
							 
							 System.out.println("ejecuto primer comando:: "+olt);
						}
						
					
					
				
					 
				 }
					System.out.println("*************************");
			}
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public respuestaDto altaOlts(requestAltaOnts datos) throws Exception {
		respuestaDto response = new respuestaDto();
		try {
			String tecnolgia = "";
			String resEstatus = "0";
			catOltsEntidad olt = catalogoOlts.getIp(datos.getIp());
			if (olt == null) {
				Integer idOlt = catalogoOlts.getIdOltMAX().getId_olt() + 1;
				catOltsEntidad olt1 = new catOltsEntidad();
				if (pruebaOLT(datos.getIp(), 1)) {
					olt1.setId_configuracion(1);
					tecnolgia = "HUAWEI";
				} else if (pruebaOLT(datos.getIp(), 2)) {
					olt1.setId_configuracion(2);
					tecnolgia = "HUAWEI";
				} else if (pruebaOLT(datos.getIp(), 3)) {
					olt1.setId_configuracion(3);
					tecnolgia = "ZTE";
				} else if (pruebaOLT(datos.getIp(), 4)) {
					olt1.setId_configuracion(4);
					tecnolgia = "ZTE";
				}
				olt1.setId_region(11);
				olt1.setId_olt(idOlt);
				//olt1.setTecnologia(tecnolgia);
				//olt1.setNombre(nombreOlt);
				olt1.setIp(datos.getIp());
				olt1.setEstatus(1);
				catalogoOlts.save(olt1);
				resEstatus = "1";
			}
			
			if (datos.getNumSerie().equals("") || datos.getNumSerie().equals(null)) {
				response.setCod(1);
				response.setSms("Favor de ingresar el Numero de Serie");
				return response;
			}
			
			response.setCod(0);
			response.setSms("Exito");
			List<datosNumeroSerieDto> dataSerie = new ArrayList<datosNumeroSerieDto>();
			datosNumeroSerieDto r = new datosNumeroSerieDto();
			r.setTipo("E");
			r.setEstatus(resEstatus);
			dataSerie.add(r);
			response.setNumeroSerie(dataSerie);
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("error al procesar la solicitud" + e);
			return response;
		}
		return response;
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
			if (util.isBlankOrNull(datos.getEstatus())) {
				//String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
				//estatus = estatusWS.equals("online") ? 1 : 2;
			} else {
				if (datos.getEstatus().equals("UP")) {
					estatus = 1;
				} else if (datos.getEstatus().equals("DOWN")) {
					estatus = 2;
				} else {
					//String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
					//estatus = estatusWS.equals("online") ? 1 : 2;
				}
			}
			if (res == null) {
				inventarioOntsTempEntidad res2 = onts2.getONT(datos.getNumSerie());
				if (res2 == null) {
					res2 = new inventarioOntsTempEntidad();
					catOltsEntidad olt = catalogoOlts.getIp(datos.getIp());
					if (olt == null) {
						Integer idOlt = catalogoOlts.getIdOltMAX().getId_olt() + 1;
						catOltsEntidad olt1 = new catOltsEntidad();
						if (pruebaOLT(datos.getIp(), 1)) {
							olt1.setId_configuracion(1);
							tecnolgia = "HUAWEI";
						} else if (pruebaOLT(datos.getIp(), 2)) {
							olt1.setId_configuracion(2);
							tecnolgia = "HUAWEI";
						} else if (pruebaOLT(datos.getIp(), 3)) {
							olt1.setId_configuracion(3);
							tecnolgia = "ZTE";
						} else if (pruebaOLT(datos.getIp(), 4)) {
							olt1.setId_configuracion(4);
							tecnolgia = "ZTE";
						}
						olt1.setId_region(11);
						olt1.setId_olt(idOlt);
						olt1.setTecnologia(tecnolgia);
						olt1.setNombre(nombreOlt);
						olt1.setIp(datos.getIp());
						olt1.setEstatus(1);
						catalogoOlts.save(olt1);
						olt = olt1;
					}
					res2.setId_region(olt.getId_region());
					res2.setNumero_serie(datos.getNumSerie());
					res2.setId_olts(olt.getId_olt());
					res2.setTipo(tipo);
					res2.setEstatus(estatus);
					res2.setSlot(Integer.parseInt(slot));
					res2.setPort(Integer.parseInt(port));
					res2.setFrame(Integer.parseInt(frame));
					res2.setDescripcionAlarma("Estado Inicial");
					res2.setTecnologia(olt.getTecnologia());
					res2.setFecha_descubrimiento(LocalDateTime.now().toString());
					res2.setLastDownTime("---");
					onts2.save(res2);
					resEstatus = res2.getEstatus().toString();
				} else {
					res2.setFecha_descubrimiento(LocalDateTime.now().toString());
					res2.setTipo(res2.getTipo() == "E" ? res2.getTipo() : tipo);
					res2.setEstatus(estatus);
					onts2.save(res2);
					resEstatus = res2.getEstatus().toString();
				}
			} else {
				res.setTipo(res.getTipo() == "E" ? res.getTipo() : tipo);
				//res.setEstatus(estatus);
				res.setActualizacion(3);
				res.setFecha_descubrimiento(LocalDateTime.now().toString());
				onts.save(res);
				resEstatus = res.getEstatus().toString();
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
			response.setSms("error al procesar la solicitud" + e);
			return response;
		}
		return response;
	}
	
	
	public boolean pruebaOLT(String ip, Integer config) {
		
		try {
			List<catOltsEntidad> olts = catalogoOlts.getConfiguracionOlt();
			 //String [] olts= {"10.180.230.12"};
			ejecucionDto respuesta=null;
			String s;
				System.out.println("*************************");
				System.out.println("olt:: "+ ip);
				String comandoZTE1 = "snmpbulkwalk  -r 1 -t 1 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x DES -X securitykey372 " + ip + " 1.3.6.1.4.1.3902.1012.3.28.1.1.5";
				String comandoZTE2 = "snmpbulkwalk  -r 1 -t 1 -v3 -u ITSMon03 -l authPriv -a SHA -A au*MGTm0n1t0r%03 -x DES -X sc#MGTm0n1t0r$30 " + ip + " 1.3.6.1.4.1.3902.1012.3.28.1.1.5";
				
				String comandoH1 = "snmpbulkwalk  -r 1 -t 1 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x AES -X sc#MGTm0n1t0r$30 " + ip + " hwGponDeviceOntSn";
				String comandoH2 = "snmpbulkwalk  -r 1 -t 1 -v3 -u ITSMon03 -l authPriv -a SHA -A au*MGTm0n1t0r%03 -x AES -X securitykey372 " + ip + " hwGponDeviceOntSn";
				
				catOtsProcesadoEntidad data=new catOtsProcesadoEntidad();
				if(config == 1) {
					respuesta=util.ejecutaComando(comandoH1);
				} else if(config == 2) {
					respuesta=util.ejecutaComando(comandoH2);
				} else if(config == 3) {
					respuesta=util.ejecutaComando(comandoZTE1);
				} else if(config == 4) {
					respuesta=util.ejecutaComando(comandoZTE2);
				}
				
				
				 if(respuesta.getBuffer()!=null) {
						while ((s = respuesta.getBuffer().readLine()) != null) {
							System.out.println("============= "+s+"=============");
							String[] parts = s.split(":");
							if(parts[0].equals("HUAWEI-XPON-MIB")) {
								return true;
							} else if (parts[0].equals("SNMPv2-SMI")){
								return true;
							} else {
								return false;
							}
						}
						respuesta.getProceso().waitFor(10, TimeUnit.SECONDS);
				 }
		
		} catch (Exception e) {
			// TODO: handle exception
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
			if(util.isBlankOrNull(datos.getEstatus())) {
				String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
				estatus = estatusWS.equals("online") ? 1 : 2;
			} else {
				if(datos.getEstatus().equals("UP")) {
					estatus = 1;
				} else if(datos.getEstatus().equals("DOWN")) {
					estatus = 2;
				} else {
					String estatusWS = pruebaONT(datos.getNumSerie(), datos.getIp());
					if(util.isBlankOrNull(estatusWS)) {
						estatus = 0;
					}else if(estatusWS.equals("online")) {
						estatus = 1;
					}else if(estatusWS.equals("ONT_no_existe")) {
						estatus = 3;
					}else {
						estatus = 2;
					}
				}
			}
			if(res == null) {
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
			response.setSms("error al procesar la solicitud"+e);
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
			HttpPost request = new HttpPost("http://10.180.199.203:8080/modelos/wizard/241?device=10.180.199.200&sh=1&port=31722&amb=pro&tkn="
					+ token);
			JSONObject params2 = new JSONObject("{\r\n"
					+ "  \"ips\": [\r\n"
					+ "        {\r\n"
					+ "            \"ipOLT\": \"" + ip + "\",\r\n"
					+ "            \"onts\": [\r\n"
					+ "                {\r\n"
					+ "                    \"ns\": \"" + numSerie + "\"\r\n"
					+ "                }\r\n"
					+ "            ]\r\n"
					+ "        }\r\n"
					+ "    ]\r\n"
					+ "}");
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


}