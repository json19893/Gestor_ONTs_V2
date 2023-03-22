package totalplay.monitor.snmp.com.negocio.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import totalplay.monitor.snmp.com.negocio.dto.oltsCMDBDto;
import totalplay.monitor.snmp.com.negocio.dto.responseDto;
import totalplay.monitor.snmp.com.negocio.dto.responsePingDto;
import totalplay.monitor.snmp.com.negocio.dto.responseToken;
import totalplay.monitor.snmp.com.negocio.service.ICronExecution;
import totalplay.monitor.snmp.com.persistencia.dao.IOltsCMDBDAO;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsDiferenciasEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsDiferenciasRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;


@Service
public class cronExecutionImpl implements ICronExecution {

	@Autowired
	private IcatOltsRepositorio catalogoOlt;


	@Autowired
	IOltsCMDBDAO<oltsCMDBDto> oltsCMDB;
	@Autowired
	IcatOltsDiferenciasRepositorio oltsDiferencia;

	//@Scheduled(fixedRate= 14400)
	public responseDto cronDescubrimientoRunStatus() {
		responseDto response = new responseDto();
		String url = "http://10.180.199.75:9080/descubrimientoByStatus";
		String url2 = "http://10.180.199.75:9080/getProcesosBase";
		//String url3 = "http://10.180.199.75:9081/getPoleoOlts";
		RestTemplate resTemplate = new RestTemplate();
		try {
			LocalDateTime ahora = LocalDateTime.now();
			System.out.println("se ejcuto: " + ahora.getHour() + ":" + ahora.getMinute());
			//ResponseEntity<String> res1 = resTemplate.getForEntity(url3, String.class);
			//if (res1.getBody().equals("ok")) {

				ResponseEntity<String> res = resTemplate.getForEntity(url, String.class);
				System.out.println("respuesta poleo/descubrimiento:::: " + res.getBody());
				if (res.getBody().equals("ok")) {
					ResponseEntity<String> rese = resTemplate.getForEntity(url2, String.class);
					System.out.println("respuesta  limpieza datos:::: " + rese);
					response.setCod(0);
					response.setSms("Exito");
				}
			//}

		} catch (Exception e) {
			response.setCod(1);
			response.setSms("Error:: " + e);
		}
		return response;
	}

	public responseDto cronMetricas() {
		responseDto response = new responseDto();
		String url = "http://localhost:9080/getmetricas";

		RestTemplate resTemplate = new RestTemplate();
		try {
			LocalDateTime ahora = LocalDateTime.now();
			System.out.println("se ejcuto: " + ahora.getHour() + ":" + ahora.getMinute());
			ResponseEntity<String> res = resTemplate.getForEntity(url, String.class);
			System.out.println("respuesta de Run Status:::: " + res.getBody());
			if (res.getBody().equals("ok")) {
				response.setCod(0);
				response.setSms("Exito");

			} else {
				response.setCod(1);
				response.setSms("Error:: " + res.getBody());
			}

		} catch (Exception e) {
			response.setCod(1);
			response.setSms("Error:: " + e);
		}
		return response;
	}

	@Override
	public boolean updateOlts() {
		List<oltsCMDBDto> olts = oltsCMDB.getOlts();
		List<catOltsEntidad> oltsList = new ArrayList<catOltsEntidad>();
		List<catOltsEntidad> oltsActuales = catalogoOlt.findAll();
		List<catOltsDiferenciasEntidad> oltsDiferencias = new ArrayList<catOltsDiferenciasEntidad>();
		catOltsDiferenciasEntidad oltDif;
		catOltsEntidad oltCatalogo;
		Integer estatus;
		Integer lastIdOlt = null;
		String tecnologia;
		Integer configuracion;

		long inicio = System.currentTimeMillis();
		String token = getToken();
		Integer contador = 0;

		try {

			for (oltsCMDBDto olt : olts) {
				contador++;
				System.out.print(contador);
				oltCatalogo = catalogoOlt.findByIp(olt.getIp_address());

				if (oltCatalogo != null) {
					System.out.print("Actualizacion olt");
					if (olt.getResource_name().compareToIgnoreCase(oltCatalogo.getNombre()) == 0) {
						// encontrar la configuracion
						configuracion = idConfiguracion(olt.getIp_address());
						// valida el estatus
						estatus = estatus = configuracion > 0 ? 1 : 0;// validarPing(token, olt.getIp_address()) ? 1:0;
						// Encuentra la tecnologìa
						if (oltCatalogo.getTecnologia() == "FIBER HOME") {
							tecnologia = "FIBER HOME";
						} else {
							tecnologia = isZTE(olt.getIp_address()) ? "ZTE" : "HUAWEI";
						}

						// oltCatalogo.setTecnologia(tecnologia);
						oltCatalogo.setId_configuracion(configuracion);
						oltCatalogo.setEstatus(estatus);
						oltCatalogo.setId_region(
								Integer.parseInt(olt.getZregion().replace("R", "").replace("COLOMBIA", "10")));

					} else {
						// TODO: actualizar los datos, el nombre es diferente

						// valida el estatus
						estatus = 0;// validarPing(token, olt.getIp_address()) ? 1:0;
						// Encuentra la tecnologìa
						tecnologia = isZTE(olt.getIp_address()) ? "ZTE" : "HUAWEI";
						// encontrar la configuracion
						configuracion = idConfiguracion(olt.getIp_address());

						oltCatalogo.setNombre(olt.getResource_name());
						oltCatalogo.setDescripcion("");
						oltCatalogo.setTecnologia(tecnologia);
						oltCatalogo.setId_region(
								Integer.parseInt(olt.getZregion().replace("R", "").replace("COLOMBIA", "10")));

						// TODO: agregar la configuraciònn
						oltCatalogo.setId_configuracion(configuracion);
						// oltCatalogo.setId_configuracion(token);
						oltCatalogo.setEstatus(estatus);
					}

				} else {

					System.out.print("Nueva olt");

					// TODO: No se encuenta registrada la ip y hay que insertar
					// Encontrar el ùltimo

					if (lastIdOlt == null) {
						lastIdOlt = catalogoOlt.findFirstByOrderByIdDesc().getId_olt() + 1;
					} else {
						lastIdOlt++;
					}

					// Encuentra la tecnologìa
					tecnologia = isZTE(olt.getIp_address()) ? "ZTE" : "HUAWEI";
					// encontrar la configuracion
					configuracion = idConfiguracion(olt.getIp_address());
					// valida el estatus
					estatus = configuracion > 0 ? 1 : 0;// validarPing(token, olt.getIp_address()) ? 1:0;

					// settear los valores
					oltCatalogo = new catOltsEntidad();
					oltCatalogo.setId_olt(lastIdOlt);
					oltCatalogo.setNombre(olt.getResource_name());
					oltCatalogo.setIp(olt.getIp_address());
					oltCatalogo.setDescripcion("");
					oltCatalogo.setTecnologia(tecnologia);
					oltCatalogo.setId_region(
							Integer.parseInt(olt.getZregion().replace("R", "").replace("COLOMBIA", "10")));

					oltCatalogo.setId_configuracion(configuracion);
					oltCatalogo.setEstatus(estatus);
				}
				if (oltCatalogo != null) {
					System.out.println(" Guardo ip " + olt.getIp_address());
					oltsList.add(oltCatalogo);
				} else {
					System.out.println("No guardò");
				}

			}
			long fin = System.currentTimeMillis();
			double tiempo = (double) ((fin - inicio) / 1000);
			System.out.println(tiempo + " segundos");
			// oltsDiferencias = oltsActuales.stream().filter(p -> !oltsList.contains(p)
			// ).collect(Collectors.toList());
			for (catOltsEntidad olt : oltsActuales) {
				if (oltsList.indexOf(olt) == -1) {// !oltsList.contains(olt)) {
					oltDif = new catOltsDiferenciasEntidad();
					oltDif.setId_configuracion(olt.getId_configuracion());
					oltDif.setDescricion(olt.getDescripcion());
					oltDif.setEstatus(olt.getEstatus());
					oltDif.setId_olt(olt.getId_olt());
					oltDif.setId_region(olt.getId_region());
					oltDif.setIp(olt.getIp());
					oltDif.setNombre(olt.getNombre());
					oltDif.setTecnologia(olt.getTecnologia());

					oltsDiferencias.add(oltDif);
				}

			}
			Collections.sort(oltsList, new Comparator<catOltsEntidad>() {
				@Override
				public int compare(catOltsEntidad p1, catOltsEntidad p2) {
					return new Integer(p1.getId_olt()).compareTo(new Integer(p2.getId_olt()));
				}
			});
			oltsDiferencia.deleteAll();
			catalogoOlt.deleteAll();
			catalogoOlt.saveAll(oltsList);
			oltsDiferencia.saveAll(oltsDiferencias);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean validarPing(String token, String ip) {
		RestTemplate resTemplate = new RestTemplate();
		resTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		String url = "http://10.180.199.203:8080/modelos/wizard/148";

		try {
			// -------------------------------------------------------------------
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);

			String builderPing = UriComponentsBuilder.fromUriString(url).queryParam("device", "10.180.199.200")
					.queryParam("sh", "1").queryParam("port", "31722").queryParam("amb", "pro").queryParam("tkn", token)
					.toUriString();
			// .queryParam("ips", "[\"ip\":\""+ip+"\", \"num_packets\": 5 ]")
			String input = "{\"ips\":[{\"ip\":\"" + ip + "\", \"num_packets\": 5 }]}";
			HttpEntity<?> entityPing = new HttpEntity<>(input, httpHeaders);

			ResponseEntity<String> responsePing = resTemplate.exchange(builderPing, HttpMethod.POST, entityPing,
					String.class);

			responsePingDto ping;
			ping = new ObjectMapper().readValue(responsePing.getBody(), responsePingDto.class);

			// System.out.println(ping.getIps().get(0).getErrorlevel()) ;
			// return ping.getIps().get(0).getReceived() == 5;
			return ping.getIps().get(0).getErrorlevel().compareTo("0") == 0;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}

	}

	public String getToken() {
		RestTemplate resTemplate = new RestTemplate();
		resTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		String url = "http://10.180.199.203:8080/ra/token/";
		try {
			// -------------------------------------------------------------------
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			String builder = UriComponentsBuilder.fromUriString(url).queryParam("App", "App").toUriString();
			HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
			// -------------------------------------------------------------------
			ResponseEntity<String> response = resTemplate.exchange(builder, HttpMethod.PUT, entity, String.class);
			responseToken token;
			token = new ObjectMapper().readValue(response.getBody(), responseToken.class);
			return token.getToken();
		} catch (Exception e) {
			return null;
		}

	}

	public boolean isZTE(String ip) {
		Process p;
		String s;
		String subcadena = "SNMPv2-SMI::enterprises";
		String comando = "snmpget -v3 -l authPriv -u userAGPON17 -a SHA -A accesskey372 -x DES -X securitykey372 " + ip
				+ " 1.3.6.1.4.1.3902.1012.3.28.2.1.3";

		try {
			p = Runtime.getRuntime().exec(comando);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			if (br != null) {
				if ((s = br.readLine()) != null) {
					s.contains(subcadena);
					return true;
				}
			}

		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public int idConfiguracion(String ip) {
		String conf1 = "snmpget  -r 2 -t 20000 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x AES -X securitykey372 "
				+ ip + " hwGponDeviceOntControlLastDownCause ";
		String conf2 = "snmpget  -r 2 -t 20000 -v3 -u ITSMon03 -l authPriv -a SHA -A au*MGTm0n1t0r%03 -x AES -X sc#MGTm0n1t0r$30 "
				+ ip + " hwGponDeviceOntControlLastDownCause";
		String conf3 = "snmpget -r 2 -t 20000 -v3 -u userAGPON17 -l authPriv -a SHA -A accesskey372 -x DES -X securitykey372 "
				+ ip + " 1.3.6.1.4.1.3902.1012.3.28.2.1.3";

		String subConf1 = "HUAWEI-XPON-MIB";
		String subConf2 = "HUAWEI-XPON-MIB";
		String subConf3 = "SNMPv2-SMI";

		String[] ips = { "10.71.1.101", "10.71.1.132", "10.180.211.34", "10.180.211.10" };

		if (Arrays.asList(ips).contains(ip)) {
			System.out.println("");
		}

		if (comparaResultado(conf1, subConf1, false)) {
			return 1;
		} /*
			 * else if(comparaResultado(conf2, subConf2, true) ) { return 2; }
			 */else if (comparaResultado(conf3, subConf3, false)) {
			return 3;
		}

		return 0;

	}

	public boolean comparaResultado(String comando, String subcadena, boolean special) {
		Process p;
		String s;

		try {

			p = Runtime.getRuntime().exec(comando);

			try {
				if (!p.waitFor(7000, TimeUnit.MILLISECONDS)) {
					// timeout - kill the process.
					p.destroy(); // consider using destroyForcibly instead
				}
			} catch (InterruptedException e) {
				p = null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			if (br != null) {
				if ((s = br.readLine()) != null) {
					s.contains(subcadena);
					return true;
				}
			}

		} catch (IOException e) {
			return false;
		}

		return false;
	}

	public boolean pruebaComando() {
		String ip = "10.71.1.132";
		String comando = "snmpget  -r 2 -t 20000 -v3 -u ITSMon03 -l authPriv -a SHA -A 'au*MGTm0n1t0r%03' -x AES -X 'sc#MGTm0n1t0r$30' "
				+ ip + " hwGponDeviceOntControlLastDownCause";
		String subcadena = "HUAWEI-XPON-MIB";
		Process p;
		String s;

		try {

			p = Runtime.getRuntime().exec(comando);

			try {
				if (!p.waitFor(11000, TimeUnit.MILLISECONDS)) {
					// timeout - kill the process.
					p.destroy(); // consider using destroyForcibly instead
				}
			} catch (InterruptedException e) {
				p = null;
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			if (br != null) {
				if ((s = br.readLine()) != null) {
					s.contains(subcadena);
					System.out.println(s);
					return true;
				}
			}

		} catch (IOException e) {
			return false;
		}

		return false;
	}
}