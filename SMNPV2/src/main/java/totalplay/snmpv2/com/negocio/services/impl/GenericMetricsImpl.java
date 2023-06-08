package totalplay.snmpv2.com.negocio.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IlimpiezaCadena;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IconfiguracionMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoAliasRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoCpuRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDownBytesRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDownPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDropDownPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDropUpPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoEstatusRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoFrameSlotPortRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastDownCauseRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastDownTimeRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastUpTimeRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoMemoryRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoProfNameRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoTimeOutRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoUpBytesRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoUpPacketsRepositorio;

import static totalplay.snmpv2.com.configuracion.Utils.getCurrentDateTime;

@Service
@Slf4j
public class GenericMetricsImpl extends Constantes implements IGenericMetrics {
	
	@Autowired
	IlimpiezaCadena limpiezaCadena;
	@Autowired 
	IinventarioOntsTempRepository inventarioTemp;
	@Autowired
	IconfiguracionMetricaRepository configuracionMetrica;
	
	@Autowired
	IpoleoLastDownCauseRepositorio poleoLastDownCause;
	@Autowired
	IpoleoLastUpTimeRepositorio poleoLastUpTime;
	@Autowired
	IpoleoLastDownTimeRepositorio poleoLastDownTime;
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
	IpoleoAliasRepositorio poleoAlias;
	@Autowired
	IpoleoProfNameRepositorio poleoProfName;
	@Autowired
	IpoleoFrameSlotPortRepositorio poleoFrameSlotPort;
	@Autowired
	IpoleoEstatusRepositorio poleoEstatus;
	@Autowired
	IinventarioOntsTempNCERepository tempNCE;
	
	
	Utils utls=new Utils();
	
	@Value("${ruta.archivo.shell}")
	private String ruta;
	@Value("${ruta.archivo.txt}")
	private String ruta2;
	@Value("${ruta.archivo.metrica}")
	private String ruta3;
	@Value("${ruta.archivo.metrica}")
	private String ruta4;
	

	@Override																					
	public  <T extends GenericPoleosDto> CompletableFuture<GenericResponseDto> poleo(configuracionDto configuracion, String idProceso, Integer metrica,Integer idOlt,Class<T> entidad, boolean saveErroneos, String referencia, boolean error,boolean manual, boolean nce) throws IOException, NoSuchFieldException, NoSuchMethodException {

		EjecucionDto proces = new EjecucionDto();
		List data = new ArrayList<T>();
		int exitValue = 1;
    	int contador = 1;
    	String tecnologia = "" ;
    	CadenasMetricasDto cadenasMetrica=null;
    	String comando;
    	String response ="";
    	boolean sinOid = false;
    	
    	try {
    	
	    	ConfiguracionMetricaEntity confMetrica = configuracionMetrica.getMetrica(metrica, configuracion.getIdConfiguracion());
			tecnologia = configuracion.getTecnologia(); 
			
			if (tecnologia.equals("ZTE")) {
				cadenasMetrica = confMetrica.getZTE();			
			} else if (tecnologia.equals("HUAWEI")){
				cadenasMetrica =  confMetrica.getHUAWEI();
			}else if (tecnologia.equals("FIBER HOME")){
				cadenasMetrica =  confMetrica.getFH();
			}
			
			if(  (cadenasMetrica == null || cadenasMetrica.getOid().equals(""))) {
				if (referencia.equals(""))
					return CompletableFuture.completedFuture(new GenericResponseDto(SIN_METRICA, 1));
				else
					sinOid = true;
			}
			
    	}catch (Exception e) {
			log.info(e.toString());
		}
    	
    	
    		
    	do{
    		try {
    						
    			if(referencia.equals("")) {
    				comando = configuracion.getComando() + SPACE + cadenasMetrica.getOid();
    			}else {
    				comando = configuracion.getComando() + SPACE + cadenasMetrica.getOid() + "." + referencia; 
    			}
    			
    			log.info(" COMANDO ------>"+comando);
    			
    			if(!error || !sinOid) {
					String logevent = configuracion.getTrazaEventos();
					logevent += "[ " + utls.getLocalDateTimeZone() + " ] "+ " INFO "+ " [Ejecutando el comando snmp]: snpmget procesando peticion..." + "\n";
					configuracion.setTrazaEventos(logevent);
					proces = utls.execBash(comando, ruta);
				}
    			
    			proces.setOid(referencia);
    			proces.setErrorOlt(error);
    			proces.setSinOid(sinOid);
				proces.setIp(configuracion.getIp());
    			
    			
    			data= limpiezaCadena.getMetricasBypoleo(proces, metrica, idOlt,
    					configuracion.getIdRegion(), idProceso, configuracion.getTecnologia(),entidad, cadenasMetrica, saveErroneos, contador,manual, nce);

    			log.info("count data "+data.size());
    			
    			if(error)
    				exitValue=0;
    			else {
    				try {
    					exitValue=proces.getProceso().exitValue();
    				}catch(Exception e){
    					exitValue = 1;
    					contador=3;
    				}	
    			}
				if(exitValue==0 || error || (contador==3 && !referencia.equals(""))){
					String logevent = configuracion.getTrazaEventos();
					logevent += "[ " + utls.getLocalDateTimeZone() + " ] "+ " INFO "+ " [Termino la Ejecuccion del Comando snmp]: " + cadenasMetrica.getOid() + "\n";
					configuracion.setTrazaEventos(logevent);
					try {
						configuracion.getManejarResultadoComando().writterLogOnDiskMetrica(ruta3, configuracion, data.get(0), 0,metrica,comando);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					guardaInventario(metrica,data,nce);
				}
			
			
			} catch (Exception e) {
				comando="";
				String logevent = configuracion.getTrazaEventos();
				logevent += "[ " + getCurrentDateTime() + " ] "+ " ERROR "+ " [Hubo un error en el proceso SNMPGET]" + "\n";
				configuracion.setTrazaEventos(logevent);
				try {
					configuracion.getManejarResultadoComando().writterLogOnDiskMetrica(ruta3, configuracion, data.get(0),1,metrica,comando);
				}catch (Exception j) {
					// TODO: handle exception
				}
				log.error(EJECUCION_ERROR, e);
				return CompletableFuture.completedFuture(new GenericResponseDto("error", 1));
			}finally {
				contador++;
			}
		}while (contador <= 3 && exitValue != 0);
    	
		if(manual){
			utls.crearArchivos(ruta2,"Total de onts : "+ data.size());
			utls.crearArchivos(ruta,DESC_FIN+configuracion.getIp());
		}else if(nce){
			utls.crearArchivos(ruta4,utls.prefixLog("Termina el proceso de descubrimiento."));
		}
    	return CompletableFuture.completedFuture(new GenericResponseDto(String.valueOf(data.size()), exitValue));
}
	@Override
	public void guardaInventario(Integer idMetrica, List list, boolean nce ) {
		switch (idMetrica) {
			case 0:
				if(nce)
					tempNCE.saveAll(list);
				else	
					inventarioTemp.saveAll(list);
			break;
			case 1:
				poleoEstatus.saveAll(list);
				break;
			case 2:
				//poleoMetrica.saveAll(list);
				poleoLastDownCause.saveAll(list);
				break;
			case 3:
				poleoLastUpTime.saveAll(list);
				break;
			case 4:
				poleoLastDownTime.saveAll(list);
				break;
			case 5:
				poleoUpBytes.saveAll(list);
				break;
			case 6:
				poleoDownBytes.saveAll(list);
				break;
			case 7:
				poleoTimeOut.saveAll(list);
				break;
			case 8:
				poleoUpPackets.saveAll(list);
				break;
			case 9:
				poleoDownPackets.saveAll(list);
				break;
			case 10:
				poleoDropUpPackets.saveAll(list);
				break;
			case 11:
				poleoDropDownPackets.saveAll(list);
				break;
			case 12:
				poleoCpu.saveAll(list);
				break;
			case 13:
				poleoMemory.saveAll(list);
				break;
			case 14:
				poleoAlias.saveAll(list);
				break;
			case 15:
				poleoProfName.saveAll(list);
				break;
			case 16:
				poleoFrameSlotPort.saveAll(list);
				break;

		}
}
}