package totalplay.snmpv2.com.negocio.services.impl;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.services.IlimpiezaCadena;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsErroneas;
import totalplay.snmpv2.com.persistencia.entidades.inventarioOntsErroneas;
import org.springframework.scheduling.annotation.Async;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IhistoricoConteoOltRepository;
import totalplay.snmpv2.com.persistencia.entidades.HistoricoConteosOltsEntity;
@Slf4j
@Service
public class LimpiezaCadenaImpl extends Constantes implements IlimpiezaCadena {
    @Autowired
	IinventarioOntsErroneas erroneasRepositori;
    @Autowired
	IcatOltsRepository catOltRepository;
    @Autowired
    IhistoricoConteoOltRepository historicoOlt;
    Utils util=new Utils();
    
    
    @Override
    public <T extends GenericPoleosDto> List<T> getMetricasBypoleo(EjecucionDto proces, Integer idmetrica,
            Integer idOlt, Integer idRegion, String IdEjecucion, String tecnologia, Class<T> entidad, CadenasMetricasDto cadenas, boolean saveErroneos )
            throws IOException {
                
    			List<T> response = new ArrayList<T>();
                String s;
                List<String> replace = util.getReplace(idmetrica, tecnologia);
                List<inventarioOntsErroneas> erroneasList=new ArrayList<inventarioOntsErroneas>();
                Integer erroneas=0;
                Integer exito=0;
                
                log.info("######################## inicio de la limpieza para OLTS: "+idOlt+" ##########################");
                
                try {
                    while ((s = proces.getBuffer().readLine()) != null) {
                           // log.info("descubrimiento valores :" + s);
                        T metrica = entidad.getConstructor().newInstance();
                        String value = s.replaceAll(replace.get(0), "");
                        String[] val = value.split(replace.get(1));
                        
                        if (idmetrica == 1) {
                            if (tecnologia.equals("ZTE")) {
                                metrica.setEstatus(val[1].trim().equals("6") ? 1 : 2);
                                metrica.setValor(val[1].trim());
                            } else if (tecnologia.equals("HUAWEI")) {
                                metrica.setEstatus(val[1].trim().equals("up(1)") ? 1 : 2);
                                metrica.setValor(val[1].trim().equals("up(1)") ? "up" : "down");
                            }else {
                                metrica.setEstatus(val[1].trim().equals("0") ? 0 :val[1].trim().equals("1")?1:val[1].trim().equals("2")?2:val[1].trim().equals("3")?3:0);
                                metrica.setValor(val[1].trim());
                            }
                        } else {
                            metrica.setEstatus(1);
                        }
                        if (val.length > 1) {
                            if (idmetrica == 16) {
                                String cadena = "";
        
                                if (val[1].contains("GPON") || val[1].contains("gpon_") || val[1].contains("xgei_")
                                        || val[1].contains("gei_")) {
                                    if (val[1].contains("GPON")) {
                                        cadena = util.getCadenaPort(val[1], "GPON");
                                    } else if (val[1].contains("gpon_")) {
                                        cadena = util.getCadenaPort(val[1], "gpon_");
                                    } else if (val[1].contains("xgei_")) {
                                        cadena = util.getCadenaPort(val[1], "xgei_");
                                    } else if (val[1].contains("gei_")) {
                                        cadena = util.getCadenaPort(val[1], "gei_");
        
                                    }
                                    String[] ifname = cadena.split("/");
                                    metrica.setPort(Integer.parseInt(ifname[2]));
                                    metrica.setSlot(Integer.parseInt(ifname[1]));
                                    metrica.setFrame(Integer.parseInt(ifname[0]));
                                    metrica.setOid(val[0].replaceAll("=", "").trim());
                                    metrica.setFecha_poleo( Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(6,ChronoUnit.HOURS)) );
                                    metrica.setId_olt(idOlt);
                                    metrica.setValor(val[1].trim());
                                    metrica.setId_metrica(idmetrica);
                                    metrica.setId_ejecucion(IdEjecucion);
                                    metrica.setId_region(idRegion);
                                    metrica.setTecnologia(tecnologia);
                                    metrica.setIndexFSP(idOlt+"-"+metrica.getId_puerto());
                                    response.add(metrica);
                                }
                            } else {
                                
                                metrica.setFecha_poleo( Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(6,ChronoUnit.HOURS)) );
                                metrica.setId_olt(idOlt);
                                if(idmetrica == 0) {
                                	
                                    exito=exito+1;
                                    String[] valOid = value.split(SPLIT_OID);	
                                    metrica.setOid(valOid[0].trim());
                                    
                                    if( tecnologia.equals("FIBER HOME") ) {
                                    	metrica.setId_puerto(valOid[0].trim());
                                    }else {
                                    	String u=valOid[0].trim().toString();
                                        u=u.replace(".", "=");
                                        String[] uid= u.split("=");		
                                        metrica.setUid(uid[1]);
                                    	 metrica.setId_puerto(uid[0]);
                                    }
                                        //metrica.setValor(val[1].trim().replaceAll("'", "").replaceAll(" ", ""));
                                    if (val.length == 2) {
                                    	metrica.setNumero_serie(val[1].trim().replaceAll("'", "").replaceAll(" ", "").replaceAll("\"", ""));
                                    	
                                    } else if(val.length == 1){
                                        metrica.setNumero_serie(val[0].trim().replaceAll("'", "").replaceAll(" ", "").replaceAll("\"", ""));
                                    } 
                                    metrica.setIndexFSP(idOlt+"-"+metrica.getId_puerto());
                                    
                                } 
                                else if (idmetrica == 2) {
                                    String dato = val[1].trim().replace("\"", "");
                                    String valor = util.getValueDownCause(dato);
                                    metrica.setOid(val[0].replaceAll("=", "").trim());
                                    metrica.setValor(valor);
                                } else {
                                    metrica.setValor(val[1].trim().replace("\"", ""));
                                    metrica.setOid(val[0].replaceAll("=", "").trim());
                                }
        
                                metrica.setId_metrica(idmetrica);
                                metrica.setId_ejecucion(IdEjecucion);
                                metrica.setId_region(idRegion);
                                metrica.setTecnologia(tecnologia);
                                metrica.setIndex(idOlt+"-"+metrica.getOid());
                                
                                response.add(metrica);
                             
                              
                            }
                        }else if(saveErroneos){
                            if (idmetrica==0){
                                erroneas=erroneas+1;
                                // log.info("vall "+val[0].trim());
	                           inventarioOntsErroneas erroneasEntidad=new inventarioOntsErroneas();
	                           erroneasEntidad.setEstatus(1);
	                           erroneasEntidad.setFecha_poleo( Date.from(ZonedDateTime.now(ZoneId.of("America/Mexico_City")).toInstant().minus(6,ChronoUnit.HOURS))  );
	                           erroneasEntidad.setId_olt(idOlt);
	                           erroneasEntidad.setId_ejecucion(IdEjecucion);
	                           erroneasEntidad.setId_region(idRegion);
	                
	                            String cadena2=val[0].trim();
	                            String[] val2 = cadena2.split(SPLIT_VALUE_STRING);
	                            if (val2.length>1){
	                                //log.info("oid:: "+val2[0].replace("=","").trim());
                                //log.info("value:: "+val2[1].trim());
                                erroneasEntidad.setValor(val2[1].replaceAll("'"," ").trim());
                                erroneasEntidad.setOid(val2[0].replace("=","").trim());
                                erroneasEntidad.setIndex(idOlt+"-"+erroneasEntidad.getOid());
                                String cadenaOid=val2[0].replace("=","").trim();
                                cadenaOid=cadenaOid.replace(".", "=");
                                String[] oid=cadenaOid.split("=");
                                if(oid.length> 1){
                                   // log.info("oid:: "+oid[0].trim());
                                    //log.info("uid:: "+oid[1].trim());
                                    erroneasEntidad.setId_puerto(oid[0].trim());
                                    erroneasEntidad.setUid(oid[1].trim());
                                }

	                            }else{
	                                //log.info("oid3:: "+val2[0].trim());
	                                erroneasEntidad.setValor(val2[0].trim());
	                                erroneasEntidad.setId_puerto(null);
	                                erroneasEntidad.setUid(null);
	                            }
                          
                            erroneasList.add(erroneasEntidad);  
                           
                        }
                      
                        }
                    }
                    proces.getProceso().waitFor(5000, TimeUnit.MILLISECONDS);
                    if(proces.getProceso().exitValue()==0 && saveErroneos){
                    	erroneasRepositori.saveAll(erroneasList);
                    }
                    if (idmetrica==0){
                        if(proces.getProceso().exitValue()==0){
                        CatOltsEntity olt=catOltRepository.getOlt(idOlt);
                        olt.setOnts_error(erroneas);
                        olt.setOnts_exito(exito);
                        olt.setDescubrio(true);
                        catOltRepository.save(olt);
                        historicoOlt.save(new HistoricoConteosOltsEntity(idOlt,LocalDateTime.now().toString(),IdEjecucion,exito,erroneas));
                        }
                    }
                    
                    proces.getProceso().destroy();
                } catch (Exception e) {
                    if (idmetrica==0){
                    CatOltsEntity olt=catOltRepository.getOlt(idOlt);
                    olt.setDescripcion(ERROR_LIMPIAR_CADENA+ e);
                    olt.setDescubrio(false);
                    catOltRepository.save(olt);
                    }
                    log.error(proces.getComando() + ":::" + ERROR_LIMPIAR_CADENA, e);
                }
                return response;
        
            }
    }
    

