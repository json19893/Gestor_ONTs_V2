package totalplay.snmpv2.com.configuracion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;

@Slf4j
public class Utils extends Constantes {
	
	@Autowired
	IcatOltsRepository catOltRepository;

	@Value("${show.snmpwalk.informatio}")
	public boolean showSnmpInformattion;
	public static configuracionDto getConfiguracion(List<CatOltsEntity> conf) {
		configuracionDto response = new configuracionDto();
		try {
			response.setIp(conf.get(0).getIp());
			response.setIdOlt(conf.get(0).getId_olt());
			response.setIdRegion(conf.get(0).getId_region());
			response.setTecnologia(conf.get(0).getTecnologia());
			response.setIdConfiguracion(conf.get(0).getId_configuracion());
			response.setNombreOlt(conf.get(0).getNombre());
			if (conf.get(0).getId_configuracion() != 0) {
				response.setVersion(conf.get(0).getConfiguracion().get(0).getVersion());
				response.setUserName(conf.get(0).getConfiguracion().get(0).getUsuario());
				response.setPassword(conf.get(0).getConfiguracion().get(0).getPassword());
				response.setLevel(conf.get(0).getConfiguracion().get(0).getNivel());
				response.setProtEn(conf.get(0).getConfiguracion().get(0).getProt_encriptado());
				response.setPhrase(conf.get(0).getConfiguracion().get(0).getFrase());
				response.setProtPriv(conf.get(0).getConfiguracion().get(0).getProt_privado());
				if (conf.get(0).getId_configuracion() != 5) {
					response.setComando(SNMP_BULK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE
							+ SPACE + response.getVersion() + USER_NAME + response.getUserName() + LEVEL
							+ response.getLevel() + PROTOCOL_ENCR + response.getProtEn() + PASSPHRASE
							+ response.getPassword() + PROTOCOL_PRIV + response.getProtPriv() + PROTOCOL_PHRASE
							+ response.getPhrase() + SPACE + response.getIp());
				} else {
					response.setComando(SNMP_WALK + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE
							+ SPACE + response.getVersion() + " -c " + response.getProtPriv() + SPACE
							+ response.getIp());
				}
			}
			response.setSms("OK");
			response.setCod(0);

		} catch (Exception e) {
			response.setSms(ERROR_OBT_CONFIGURACION);
			response.setCod(1);
		}
		return response;
	}

	public static String getMetrica(String tecnologia, int metrica) {
		String oid = "";
		switch (metrica) {
		case 0:
			oid =  tecnologia.equals("ZTE" )? OID_NUM_SERIE_ZTE
					:  tecnologia.equals("HUAWEI" )? OID_NUM_SERIE_HUAWEI : OID_NUM_SERIE_FIBER_HOME;
			break;
			case 1:
			oid =  tecnologia.equals("ZTE" )? OID_RUN_STATUS_ZTE
					:  tecnologia.equals("HUAWEI" )? OID_RUN_STATUS_HUAWEI : OID_RUN_STATUS_FIBER_HOME;
			break;
		}

		return oid;
	}

	public  EjecucionDto execBash(String comando, String ruta) throws IOException {
		EjecucionDto response = new EjecucionDto();
		
		response.setSms(EJECUCION_EXITOSA);
		response.setCod(0);
		Process	p;
		try {
			String shell = ruta  + Thread.currentThread().getId() + ".sh";
		
			File file = new File(shell);
			response.setFile(shell);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(comando);
			bw.close();
			ProcessBuilder pb = new ProcessBuilder(new String[] { "sh", "-c", "chmod 777 " + shell + ";" + shell });
			p= pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			response.setBuffer(br);
			response.setProceso(p);
			response.setComando(comando);
		} catch (Exception e) {
			response.setSms(ERROR_EJECUTAR_COMANDO);
			response.setCod(1);

		}

		return response;

	}
	
	public  List<String> getReplace(Integer idMetrica, String tecnologia) {
		List<String> response = new ArrayList<String>();
		switch (idMetrica) {
		case 0:
			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_NUM_SERIE_CADENA_ZTE);
				response.add(SPLIT_NUM_SERIE_VAL2);
			} else if (tecnologia.equals("HUAWEI")) {
				response.add(SPLIT_NUM_SERIE_CADENA_HUAWEI);
				response.add(SPLIT_NUM_SERIE_VAL2);
			}else {
				response.add(SPLIT_NUM_SERIE_CADENA_FIBER_HOME);
				response.add(SPLIT_VALUE_STRING);
			}
			break;
			
		case 1:
			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_RUN_STATUS_CADENA_ZTE);
				response.add(SPLIT_VALUE_INTEGER);
			} else if (tecnologia.equals("HUAWEI")) {
				response.add(SPLIT_RUN_STATUS_CADENA_HUAWEI);
				response.add(SPLIT_VALUE_INTEGER);
			}else {
				response.add(SPLIT_RUN_STATUS_CADENA_FIBER_HOME);
				response.add(SPLIT_VALUE_INTEGER);
			}
			break;
		case 2:
			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_LAST_DOWN_CAUSE_CADENA_ZTE);
				response.add(SPLIT_VALUE_INTEGER);
			} else {
				response.add(SPLIT_LAST_DOWN_CAUSE_CADENA_HUAWEI);
				response.add(SPLIT_VALUE_INTEGER);
			}

			break;
		case 3:
			response.add(SPLIT_LAST_UP_TIME);
			response.add(SPLIT_VALUE_STRING);
			break;
		case 4:
			response.add(SPLIT_LAST_DOWN_TIME);
			response.add(SPLIT_VALUE_STRING);
			break;
		case 5:
			response.add(SPLIT_UP_BYTES);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 6:
			response.add(SPLIT_DOWN_BYTES);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 7:
			response.add(SPLIT_TIME_OUT);
			response.add(SPLIT_VALUE_INTEGER);
			break;
		case 8:
			response.add(SPLIT_UP_PAKETS_CADENA);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 9:
			response.add(SPLIT_DOWN_PAKETS_CADENA);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 10:
			response.add(SPLIT_DROP_UP_PAKETS_CADENA);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 11:
			response.add(SPLIT_DROP_DOW_PAKETS_CADENA);
			response.add(SPLIT_VALUE_COUNTER64);
			break;
		case 12:
			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_CPU_CADENA_ZTE);
				response.add(SPLIT_VALUE_INTEGER);
			} else {
				response.add(SPLIT_CPU_CADENA);
				response.add(SPLIT_VALUE_INTEGER);
			}

			break;
		case 13:

			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_MEMORY_CADENA_ZTE);
				response.add(SPLIT_VALUE_INTEGER);
			} else {
				response.add(SPLIT_MEMORY_CADENA);
				response.add(SPLIT_VALUE_INTEGER);
			}

			break;
		case 14:
			if (tecnologia.equals("ZTE")) {
				response.add(SPLIT_ALIAS_ONT_ZTE);
				response.add(SPLIT_VALUE_STRING);
			} else {
				response.add(SPLIT_ALIAS_ONT_HUAWEI);
				response.add(SPLIT_VALUE_STRING);
			}

			break;
		case 15:
			response.add(SPLIT_PROF_NAME_ONT);
			response.add(SPLIT_VALUE_STRING);
			break;
		case 16:
			if (tecnologia.equals("FIBER HOME")) {
				response.add(SPLIT_FRAME_SLOT_PORT_FIBER_HOME);
				response.add(SPLIT_VALUE_STRING);				
			}else {
				response.add(SPLIT_FRAME_SLOT_PORT);
				response.add(SPLIT_VALUE_STRING);
			}
			break;

		}
		return response;
	}
	
	
	
	public String getCadenaPort(String cadena, String remplazar) {

		String response = cadena.replaceFirst(remplazar, "").replace("\"", "").trim();
		return response;
	}
	public String getValueDownCause(String valor) {
		String response = "";
		try {

			switch (valor) {
			case "1":
				response = CAUSE_1;
				break;
			case "2":
				response = CAUSE_2;
				break;
			case "3":
				response = CAUSE_3;
				break;
			case "4":
				response = CAUSE_4;
				break;
			case "5":
				response = CAUSE_5;
				break;
			case "6":
				response = CAUSE_6;
				break;
			case "7":
				response = CAUSE_7;
				break;
			case "8":
				response = CAUSE_8;
				break;
			case "9":
				response = CAUSE_9;
				break;
			case "10":
				response = CAUSE_10;
				break;
			case "11":
				response = CAUSE_11;
				break;
			case "12":
				response = CAUSE_12;
				break;
			case "13":
				response = CAUSE_13;
				break;
			case "14":
				response = CAUSE_14;
				break;
			case "15":
				response = CAUSE_15;
				break;
			case "-1":
				response = CAUSE_16;
				break;
			case "18":
				response = CAUSE_18;
				break;
			case "30":
				response = CAUSE_30;
				break;
			case "31":
				response = CAUSE_31;
				break;
			case "32":
				response = CAUSE_32;
				break;
			case "33":
				response = CAUSE_33;
				break;
			case "34":
				response = CAUSE_34;
				break;
			case "35":
				response = CAUSE_35;
				break;
			case "37":
				response = CAUSE_37;
				break;
			case "38":
				response = CAUSE_38;
				break;
			case "39":
				response = CAUSE_39;
				break;
			case "40":
				response = CAUSE_40;
				break;
			default:
				response = CAUSE_DEFAULT;
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;

	}

	public boolean vaidaPin(String ip) throws IOException, InterruptedException {
		String s;
		boolean response = false;
		InetAddress ping;

		try {
			ping = InetAddress.getByName(ip);
			if (ping.isReachable(5000)) {
				System.out.println(ip + " - responde!");
				response = true;
			} else {
				System.out.println(ip + " - no responde!");
				response = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("error:" + e);
		}
		return response;
	}
	
	
	

}
