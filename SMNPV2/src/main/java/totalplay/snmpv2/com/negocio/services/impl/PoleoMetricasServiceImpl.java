package totalplay.snmpv2.com.negocio.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.PoleoMetricasUtilsService;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.*;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.*;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.*;
import org.springframework.scheduling.annotation.Async;
import totalplay.snmpv2.com.persistencia.repositorio.IauxiliarJoinEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IconfiguracionMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IhistoricoConteoOltRepository;
import totalplay.snmpv2.com.presentacion.MetricaController.MetricaPoleo;
import totalplay.snmpv2.com.persistencia.entidades.PoleosAliasEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosCpuEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDownBytesEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDownPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDropDownPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosDropUpPacketsEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosFrameSlotPortEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastDownCauseEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastDownTimeEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosLastUpTimeEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosMemoryEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosProfNameEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosTimeOutEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosUpBytesEntity;
import totalplay.snmpv2.com.persistencia.entidades.PoleosUpPacketsEntity;


@Slf4j
@Service
public class PoleoMetricasServiceImpl extends Constantes implements IpoleoMetricasService {
    
	@Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    IinventarioOntsErroneas erroneasRepositori;
    @Autowired
    IcatOltsRepository catOltRepository;
    @Autowired
    IhistoricoConteoOltRepository historicoOlt;
    @Autowired
    IGenericMetrics genericMetrics;
    @Autowired
    IconfiguracionMetricaRepository configuracionMetrica;
    @Autowired
    ImonitorPoleoOltMetricaRepository monitorPoleoOltMetrica;
    @Autowired
    IinventarioOntsRepository inventario;
    @Autowired
    IasyncMethodsService asyncMethods;
    @Autowired
    IauxiliarJoinEstatusRepository auxiliarJoinEstatus;
    @Autowired
    IfaltantesMetricasRepository faltantesMetricas;
    @Autowired
    IfaltantesMetricasManualRepository faltantesMetricasManual;
    @Autowired
    IfaltantesEstatusRepository faltantesEstatus;
   
    /*----------------Se inyectan las dependencias para las mètricas----------------*/

    @Autowired
    IpoleoLastDownCauseRepositorio poleoLastDownCauseRe;
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
    IpoleoAliasRepositorio poleoAlias;
    @Autowired
    IpoleoProfNameRepositorio poleoProfName;
    @Autowired
    IpoleoFrameSlotPortRepositorio poleoFrameSlotPort;
    @Autowired
    IpoleoLastDownTimeRepositorio poleoLastDownTimeRepo;
    @Autowired
    IpoleoEstatusRepositorio poleoEstatus;

    @Autowired
    PoleoMetricasUtilsService poleoMetricasUtilsService;
    @Autowired
    ImonitorPoleoRepository monitorPoleo;
    Utils util = new Utils();


    @Override

    @Async("taskExecutor2")
    public CompletableFuture<String> executeProcess(List<CatOltsEntity> olts, String idMonitorPoleo, int idMetrica) throws Exception {

        Date fechaInicio = util.getDate();
        String hilo = Thread.currentThread().getName();
        Integer estatusPoleo = FALLIDO;
        ConfiguracionMetricaEntity confMetrica;
        MonitorPoleoOltMetricaEntity monitor;
        String idMonitorOlt = "";


        try {

            for (CatOltsEntity olt : olts) {
                try {

                    Integer idOlt = olt.getId_olt();
                    monitor = monitorPoleoOltMetrica.getMonitorExist(idMonitorPoleo, idMetrica, olt.getId_olt());

                    if (monitor != null)
                        idMonitorOlt = monitor.getId();
                    else
                        idMonitorOlt = monitorPoleoOltMetrica.save(new MonitorPoleoOltMetricaEntity(idOlt, Integer.valueOf(idMetrica), util.getDate(), idMonitorPoleo)).getId();


                    AggregationOperation match = Aggregation.match(Criteria.where("id_olt").is(idOlt));
                    AggregationOperation lookup = Aggregation.lookup("cat_configuracion", "id_configuracion",
                            "id_configuracion", "configuracion");
                    Aggregation aggregation = Aggregation.newAggregation(match, lookup);
                    AggregationResults<CatOltsEntity> out = mongoTemplate.aggregate(aggregation, "cat_olts",
                            CatOltsEntity.class);
                    configuracionDto configuracion = util.getConfiguracion(out.getMappedResults());


                    CompletableFuture<GenericResponseDto> metrica = new CompletableFuture<GenericResponseDto>();


                    switch (idMetrica) {
                        case 1:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 2:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 3:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 4:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 5:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 6:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 7:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 8:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 9:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 10:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 11:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 12:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 13:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 14:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 15:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, "", false, false, false, false, "");
                            break;
                        case 16:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, "", false, false, false, false, "");
                            break;
                    }


                    MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
                    monitorPoleoOlt.setFecha_inicio(fechaInicio);
                    monitorPoleoOlt.setFecha_fin(util.getDate());
                    monitorPoleoOlt.setError(metrica.get().getSms().equals("0") || metrica.get().getSms().equals("error") || metrica.get().getSms().equals("Sin metrica"));
                    monitorPoleoOlt.setResultado(metrica.get().getSms());

                    monitorPoleoOltMetrica.save(monitorPoleoOlt);

                } catch (Exception e) {
                    log.error("error", e);

                    if (!idMonitorOlt.equals("")) {
                        MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
                        monitorPoleoOlt.setFecha_inicio(fechaInicio);
                        monitorPoleoOlt.setFecha_fin(util.getDate());
                        monitorPoleoOlt.setError(true);

                        monitorPoleoOltMetrica.save(monitorPoleoOlt);
                    }

                }

                estatusPoleo = EXITOSO;

            }

        } catch (Exception e) {
            log.error("error:" + e);
        }

        return null;

    }


    @Override
    public List<OntsConfiguracionDto> getOntsFaltantes(int idMetrica, String idEjecucion, boolean resultado, boolean empresariales, String tabla, int tipo, List<CatOltsEntity> olts, Integer olt) {

        List<OntsConfiguracionDto> faltantes = null;

        try {
            switch (idMetrica) {
                case 1:
                	if(olt!= null )
                		poleoEstatus.outToAux(idEjecucion, tabla,true, olt  );
                	else
                        poleoEstatus.outToAux(idEjecucion, tabla,false,null );
                    break;
                case 2:
                    poleoLastDownCauseRe.outToAux(idEjecucion, tabla);
                    break;
                case 3:
                    poleoLastUpTime.outToAux(idEjecucion, tabla);
                    break;
                case 4:
                    poleoLastDownTimeRepo.outToAux(idEjecucion, tabla);
                    break;
                case 5:
                    poleoUpBytes.outToAux(idEjecucion, tabla);
                    break;
                case 6:
                    poleoDownBytes.outToAux(idEjecucion, tabla);
                    break;
                case 7:
                    poleoTimeOut.outToAux(idEjecucion, tabla);
                    break;
                case 8:
                    poleoUpPackets.outToAux(idEjecucion, tabla);
                    break;
                case 9:
                    poleoDownPackets.outToAux(idEjecucion, tabla);
                    break;
                case 10:
                    poleoDropUpPackets.outToAux(idEjecucion, tabla);
                    break;
                case 11:
                    poleoDropDownPackets.outToAux(idEjecucion, tabla);
                    break;
                case 12:
                    poleoCpu.outToAux(idEjecucion, tabla);
                    break;
                case 13:
                    poleoMemory.outToAux(idEjecucion, tabla);
                    break;
                case 14:
                    poleoAlias.outToAux(idEjecucion, tabla);
                    break;
                case 15:
                    poleoProfName.outToAux(idEjecucion, tabla);
                    break;
                case 16:
                	if(olt!=null)
                		poleoFrameSlotPort.outToAux(idEjecucion, tabla, true, olt);
                	else
                		poleoFrameSlotPort.outToAux(idEjecucion, tabla, false, null);
                    break;
            }
        } catch (Exception e) {
            log.error("error", e);
        }
        if (resultado) {
            try {
                if (empresariales) {
                    if (idMetrica == 1)
                        faltantes = inventario.findOntsEmpresarialesMetricasEstatus(tabla);
                    else if (idMetrica != 16)
                        faltantes = inventario.findOntsEmpresarialesMetricas(tabla);
                    else
                        faltantes = inventario.findOntsEmpresarialesMetricasFrameSlotPort(tabla);
                } else {
                    faltantes = getFaltantes(idMetrica, tabla, tipo, idEjecucion, olts);
                }
            } catch (Exception e) {
                log.error("error", e);
            }
            return faltantes;
        }
        return null;

    }

    @Async("taskExecutor")
    public CompletableFuture<String> getMetricaEmpresarialesByMetrica(List<OntsConfiguracionDto> ontsEmpresariales,
                                                                      String idMonitorPoleo, int idMetrica, boolean oltNCE) {

        for (OntsConfiguracionDto ont : ontsEmpresariales) {
            EjecucionDto proces = null;
            try {

                CatConfiguracionEntity conf = ont.getConfiguracion();
                String oid = idMetrica != 16 ? ont.getOnt().getOid() : ont.getOnt().getId_puerto();
                Integer idOlt = ont.getOnt().getId_olt();
                String tecnologia = ont.getOnt().getTecnologia();
                Integer region = ont.getOnt().getId_region();
                Integer configuracion = ont.getId_configuracion();
                String comando;
                boolean errorOlt = false;

                if (ont!=null && (ont.getPoleable()!=null) && (ont.isError() || ont.getPoleable().intValue() == 0))
                    errorOlt = true;

                log.info("Porcesando la ont con id: " + oid);

                if (oid != null && ont.getId_configuracion() != 0 && !ont.getIp().equals("")) {

                    if (tecnologia != "FIBER HOME") {
                        comando = SNMP_GET + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + "2" + SPACE
                                + conf.getVersion() + USER_NAME + conf.getUsuario() + LEVEL + conf.getNivel()
                                + PROTOCOL_ENCR + conf.getProt_encriptado() + PASSPHRASE + conf.getPassword()
                                + PROTOCOL_PRIV + conf.getProt_privado() + PROTOCOL_PHRASE + conf.getFrase() + SPACE + IR
                                + ont.getIp() + SPACE;
                    } else {
                        comando = SNMP_GET + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + "2"
                                + SPACE + conf.getVersion() + " -c " + conf.getProt_privado() + SPACE
                                + ont.getIp() + SPACE;
                    }
                    configuracionDto configPoleo = new configuracionDto();
                    configPoleo.setComando(comando);
                    configPoleo.setIdConfiguracion(configuracion);
                    configPoleo.setTecnologia(tecnologia);

                    // TODO: LLAMAR A LOS COMANDOS PARA LAS MÈTRICAS
                    CompletableFuture<GenericResponseDto> metrica = new CompletableFuture<GenericResponseDto>();
                    try {
                        switch (idMetrica) {
                            case 1:
                            	if(oltNCE)
                            		metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 1, idOlt, PoleoEstatusOltsNCEEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                            	else
                            		metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                
                                break;
                            case 2:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 3:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 4:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 5:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 6:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 7:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 8:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 9:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 10:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 11:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 12:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 13:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 14:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 15:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                            case 16:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, oid, errorOlt, false, false, oltNCE, "");
                                break;
                        }

                    } catch (Exception e) {
                        log.error("error", e);
                    }
                }

            } catch (Exception e) {
                log.error("error", e);
            }
        }

        return null;

    }

    @Override
    public String joinUpdateStatus(String idMonitorPoleo) {
    	String estatus="";
        log.info("::::::::::::::::::::::::::::::::::::Inicia la actualizaciòn de estatus del inventario final");

        getOntsFaltantes(1, idMonitorPoleo, false, true, "auxiliar_estatus", 2, null, null);


        try {
            List<CatOltsEntity> oltsInventario = inventario.getOlts();
            ArrayList<CompletableFuture<GenericResponseDto>> regionSegmentOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
            auxiliarJoinEstatus.deleteAll();

            Integer maxOnts = (oltsInventario.size() / 42) + 1;

            maxOnts = (oltsInventario.size() / 42) + 1;

            for (int i = 0; i < oltsInventario.size(); i += maxOnts) {
                Integer limMax = i + maxOnts;
                if (limMax >= oltsInventario.size()) {
                    limMax = oltsInventario.size();
                }
                List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(
                        oltsInventario.subList(i, limMax));

                CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.joinUpdateStatus(listSegment);
                regionSegmentOlts.add(executeProcess);
            }

            CompletableFuture.allOf(regionSegmentOlts.toArray(new CompletableFuture[regionSegmentOlts.size()])).join();
            
            if(auxiliarJoinEstatus.count()<inventario.count()) {
            	//Se completa el inventario
                 completarInventarioJoin();
            }
            
            
            try {
            	if(auxiliarJoinEstatus.count() == inventario.count()) {
            		auxiliarJoinEstatus.sendToInventario();
            		estatus = "Cruce realizado correctamente";
            	}else
            		estatus =  "Inventario Corrompido, no se relizó en enlace";	
            } catch (Exception e) {
            	estatus = "No se pudo realizar el cruce con el inventario";
            }
            

        } catch (Exception e) {
            estatus = "No se pudo realizar el cruce con el inventario";
        }
        
        return estatus;
        

    }

    public List<OntsConfiguracionDto> getFaltantes(int idMetrica, String tablaJoin, int tipo, String idEjecucion, List<CatOltsEntity> olts) {

        List<OntsConfiguracionDto> respuesta = null;
        String joinField = "index";

        try {

            if (idMetrica == 16)
                joinField = "indexFSP";

            List<CatOltsEntity> oltsInventario;

            if (olts != null) {
                oltsInventario = olts;
            } else {
                oltsInventario = inventario.getOlts();
            }

            ArrayList<CompletableFuture<GenericResponseDto>> regionSegmentOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();

            Integer maxOnts = (oltsInventario.size() / 42) + 1;

            maxOnts = (oltsInventario.size() / 1);// + 1;

            for (int i = 0; i < oltsInventario.size(); i += maxOnts) {
                Integer limMax = i + maxOnts;
                if (limMax >= oltsInventario.size()) {
                    limMax = oltsInventario.size();
                }
                List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(
                        oltsInventario.subList(i, limMax));

                CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.getFaltantesMetricas(listSegment, tablaJoin, joinField, tipo, idEjecucion, idMetrica);
                regionSegmentOlts.add(executeProcess);
            }

            CompletableFuture.allOf(regionSegmentOlts.toArray(new CompletableFuture[regionSegmentOlts.size()])).join();

            switch (tipo) {
                case 1:
                    respuesta = faltantesMetricasManual.getAll();
                    faltantesMetricasManual.deleteAll();
                    break;
                case 2:
                    respuesta = faltantesMetricas.getAll();
                    faltantesMetricas.deleteAll();
                    break;
                case 3:
                    respuesta = faltantesEstatus.getAll();
                    faltantesEstatus.deleteAll();
                    break;

            }


        } catch (Exception e) {
            log.error("error", e);
        }

        return respuesta;
    }

    /**
     * Esta funcion consulta mediante snmpget una metrica de una ont
     *
     * @param request
     * @return
     */
    @Override
    public GenericResponseDto getPoleoOntMetrica(RequestPostMetrica request) throws Exception {
        String logEventos = "";
        logEventos = "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Ejecutando Poleo Manual]:  PoleoMetricasImpl.getPoleoOntMetrica" + "\n";

        //System.out.println(logEventos);
        //Parametros de entrada:
        String num_serie = request.getNumero_serie();
        Integer idMetrica = request.getIdMetrica();
        Date fechaInicio = Date.from(util.getLocalDateTimeZone().atZone(ZoneId.systemDefault()).toInstant());

        //Mensaje del happy path
        GenericResponseDto response = new GenericResponseDto();
        response.setCod(0);
        response.setSms("Exito: Se poleo la metrica correctamente");


        String OID_METRICA = "";
        Utils utilerias;

        InventarioOntsEntity ont;
        CatOltsEntity olt;
        configuracionDto configuracionPoleo;

        ont = inventario.getOntBySerialNumber(num_serie);
        OntsConfiguracionDto config;
        if (ont == null) {
            response.setCod(RESOURCE_NOT_FOUND);
            response.setSms("Error: recurso no encontrado");
            return response;
        }

        String tecnologia = ont.getTecnologia();

        olt = catOltRepository.getOlt(ont.getId_olt());
        if (olt == null) {
            response.setCod(RESOURCE_NOT_FOUND);
            response.setSms("Error: recurso no encontrado");
            return response;
        }

        if (olt.getEstatus().equals("0")) {
            response.setCod(RESOURCE_NOT_AVAILABLE);
            response.setSms("Error: recurso no disponible");
            return response;
        }
        Integer idConfiguracion = olt.getId_configuracion();

        Query query = new Query();
        query.addCriteria(Criteria.where("id_metrica").is(idMetrica.intValue()));
        ConfiguracionMetricaEntity configMetrica = mongoTemplate
                .findOne(query, ConfiguracionMetricaEntity.class, "tb_configuracion_metricas");
        if (configMetrica == null) {
            response.setCod(METRIC_NOT_SUPPORTED);
            response.setSms("Error: Metrica no soportada por esta version");
            return response;
        }
        //Enununciado completo: full-oid :oid_tecnologia + oid_ont + uid;
        //oid_ont + uid;
        if (configMetrica.isActivo()) {
            if (ont.getTecnologia().equalsIgnoreCase("HUAWEI")) {
                OID_METRICA = ont.getOid();// + "." + ont.getUid();
            }

            if (ont.getTecnologia().equalsIgnoreCase("ZTE")) {
                OID_METRICA = ont.getOid();// + "." + ont.getUid();
            }

            if (ont.getTecnologia().equalsIgnoreCase("FH")) {
                OID_METRICA = ont.getOid();// + "." + ont.getUid();
            }
            //Caso especial: Poleo de FRAME
            if (idMetrica == FRAME_SLOT_PORT) {
                OID_METRICA =ont.getOid().split("\\.")[0];
            }
        }

        AggregationOperation match
                = Aggregation.match(Criteria.where("id_olt").is(olt.getId_olt()));
        AggregationOperation lookup
                = Aggregation.lookup("cat_configuracion", "id_configuracion",
                "id_configuracion", "configuracion");
        Aggregation aggregation = Aggregation.newAggregation(match, lookup);
        AggregationResults<CatOltsEntity> out
                = mongoTemplate.aggregate(aggregation, "cat_olts", CatOltsEntity.class);

        utilerias = new Utils();
        configuracionPoleo = utilerias.getConfiguracion(out.getMappedResults());
        System.out.println(ont);

        final String BASE_COMMAND = SNMP_GET + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE
                + SPACE + configuracionPoleo.getVersion() + USER_NAME + configuracionPoleo.getUserName() + LEVEL
                + configuracionPoleo.getLevel() + PROTOCOL_ENCR + configuracionPoleo.getProtEn() + PASSPHRASE
                + configuracionPoleo.getPassword() + PROTOCOL_PRIV + configuracionPoleo.getProtPriv()
                + PROTOCOL_PHRASE + configuracionPoleo.getPhrase() + SPACE + IR + olt.getIp();

    
        //String idMonitorPoleo = monitorPoleo.findFirstByOrderByIdDesc().getId();
        String idMonitorPoleo = monitorPoleo.getLastFinishId().getId();

        configuracionPoleo.setComando(BASE_COMMAND);
        configuracionPoleo.setIdConfiguracion(idConfiguracion);
        configuracionPoleo.setTecnologia(tecnologia);
        configuracionPoleo.setTrazaEventos(logEventos);

        String finalLogEventos = logEventos;
        //Escucha los eventos
        configuracionPoleo.setManejarResultadoComando((rutaArchivo, conf, result, status, metrica, comando) -> {
            util.deleteLogFile(rutaArchivo);
            String copyString = finalLogEventos;
            String valor = "";
            try {
                valor = obtenerValor(metrica, result, ont);
                log.info("################# ::" + valor);
                String tmp = conf.getTrazaEventos();
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Datos Generales del Poleo]" + "\n";
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Id Olt]: " + ont.getId_olt() + "\n";
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Ont Número_Serie]: " + ont.getNumero_serie() + "\n";
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Ont Tecnología]: " + ont.getTecnologia() + "\n";
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Id-Metrica]: " + configMetrica.getId_metrica() + "\n";
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Nombre-Metrica]: " + configMetrica.getNombre() + "\n";
                if (status == 0) {
                    tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Comando Ejecutado]: " + "\n";
                    tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO   " + comando + "\n";
                    tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Resultado Ejecucción]: " + valor + "\n";
                    conf.setTrazaEventos(tmp);
                    response.setCod(0);
                    response.setSms("Se ejecuto correctamente la metrica: " + metrica.intValue());
                } else {
                    tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " ERROR " + " [Error en la ejecucción del comando]: " + comando + "\n";
                    conf.setTrazaEventos(tmp);
                    response.setCod(1);
                    response.setSms("Error en la ejecuccion del comando: " + metrica.intValue());
                }
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Guardando en Disco]: " + rutaArchivo + "\n";
                //Persistir log resultante del proceso del poleo:
                tmp += "[ " + util.getLocalDateTimeZone() + " ] " + " INFO " + " [Finalizó la Ejecucción del Poleo Manual]:  PoleoMetricasImpl.getPoleoOntMetrica" + "\n";
                //Persiste en disco en un log:
                conf.setTrazaEventos(tmp);
                util.crearArchivos(rutaArchivo, conf.getTrazaEventos());

            } catch (Exception e) {
                log.error("Error: " + e);
                response.setCod(1);
                response.setSms("Error: Error en el casteo de la metrica: " + metrica.intValue());
            }
        });

        CompletableFuture<GenericResponseDto> metricaAsyncProcess = new CompletableFuture<>();
        boolean errorOlt = false;

        metricaAsyncProcess = poleoMetricasUtilsService
                .dispatcherAsyncPoleoMetrica(configuracionPoleo, idMonitorPoleo, request.getIdMetrica(),
                        olt.getId_olt(), false, OID_METRICA, false, false); //Se detiene el hilo principal hasta que finalice el hilo ejecutado en paralelo:
        GenericResponseDto asyncResponse = metricaAsyncProcess.get();

        boolean isErrorAsyncProcess
                = asyncResponse.getSms().equals("error") || asyncResponse.getSms().equals("Sin metrica");

        if(isErrorAsyncProcess){
            asyncResponse.setCod(1);
            asyncResponse.setSms("Error: En la ejecuccion de la metrica: " + idMetrica.intValue());
        }

        //Settea la respuesta
        asyncResponse.setCod(0);
        asyncResponse.setSms("Se poleo correctamente la Métrica " + "[id: "+idMetrica.intValue() + ", Nombre: " +  configMetrica.getNombre() + "]");

        Date fechaFin = Date.from(util.getLocalDateTimeZone().atZone(ZoneId.systemDefault()).toInstant());

        MonitorPoleoOltMetricaEntity monitor;
        String idMonitorOlt = "";

        /*monitor = monitorPoleoOltMetrica.getMonitorExist(idMonitorPoleo, idMetrica, olt.getId_olt());
        if (monitor != null)
            idMonitorOlt = monitor.getId();
        else
            idMonitorOlt = monitorPoleoOltMetrica.save(new MonitorPoleoOltMetricaEntity(idOlt, Integer.valueOf(idMetrica), fechaFin, idMonitorPoleo)).getId();

        MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
        monitorPoleoOlt.setFecha_inicio(fechaInicio);
        monitorPoleoOlt.setFecha_fin(fechaFin);
        monitorPoleoOlt.setError(isErrorAsyncProcess);
        monitorPoleoOlt.setResultado(metricaAsyncProcess.get().getSms());*/
        return asyncResponse;
    }

    private String obtenerValor(Integer metrica, Object result, InventarioOntsEntity ont) {
        String valor = "";
        InventarioOntsEntity inv = ont;

        switch (metrica) {
            case RUN_STATUS:
                PoleosEstatusEntity me = (PoleosEstatusEntity) result;
                valor = me.getValor();
                inv.setEstatus(
                        me.getValor().equals("up(1)") ? 1:
                                me.getValor().equals("down(2)")? 2:
                                        me.getValor().equals("6")?1:
                                                me.getValor().equals("1")?1:
                                                        me.getValor().equals("2")?2:
                                                                me.getValor().equals("3")?3:2);
                inv.setFecha_modificacion(util.getDate());
                break;
            case LAST_DOWN_CASE:
                PoleosLastDownCauseEntity me1 = (PoleosLastDownCauseEntity) result;
                valor = me1.getValor();
                inv.setDescripcionAlarma(me1.getValor());
                inv.setFecha_modificacion(util.getDate());
                break;
            case LAST_UP_TIME:
                PoleosLastUpTimeEntity me2 = (PoleosLastUpTimeEntity) result;
                valor = me2.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case LAST_DOWN_TIME:
                PoleosLastDownTimeEntity me3 = (PoleosLastDownTimeEntity) result;
                valor = me3.getValor();
                inv.setLastDownTime(me3.getValor());
                inv.setFecha_modificacion(util.getDate());
                break;
            case UP_BYTES:
                PoleosUpBytesEntity me4 = (PoleosUpBytesEntity) result;
                valor = me4.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case DOWN_BYTES:
                PoleosDownBytesEntity me5 = (PoleosDownBytesEntity) result;
                valor = me5.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case TIMEOUT:
                PoleosTimeOutEntity me6 = (PoleosTimeOutEntity) result;
                valor = me6.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case UP_PACKETS:
                PoleosUpPacketsEntity me7 = (PoleosUpPacketsEntity) result;
                valor = me7.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case DOWN_PACKETS:
                PoleosDownPacketsEntity me8 = (PoleosDownPacketsEntity) result;
                valor = me8.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case DROP_UP_PACKETS:
                PoleosDropUpPacketsEntity me9 = (PoleosDropUpPacketsEntity) result;
                valor = me9.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case DROP_DOWN_PACKETS:
                PoleosDropDownPacketsEntity me10 = (PoleosDropDownPacketsEntity) result;
                valor = me10.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case CPU:
                PoleosCpuEntity me11 = (PoleosCpuEntity) result;
                valor = me11.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case MEMORY:
                PoleosMemoryEntity me12 = (PoleosMemoryEntity) result;
                valor = me12.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case ALIAS_ONT:
                PoleosAliasEntity me13 = (PoleosAliasEntity) result;
                valor = me13.getValor();
                inv.setAlias(me13.getValor());
                inv.setFecha_modificacion(util.getDate());
                break;
            case PROF_NAME_ONT:
                PoleosProfNameEntity me14 = (PoleosProfNameEntity) result;
                valor = me14.getValor();
                inv.setFecha_modificacion(util.getDate());
                break;
            case FRAME_SLOT_PORT:
                PoleosFrameSlotPortEntity me15 = (PoleosFrameSlotPortEntity) result;
                valor = me15.getFrame()+"/"+me15.getSlot()+"/"+me15.getPort();
                inv.setFrame(me15.getFrame());
                inv.setSlot(me15.getSlot());
                inv.setPort(me15.getPort());
                inv.setFecha_modificacion(util.getDate());
                break;
        }
        if (inv!=null){
            inventario.save(inv);
        }
        return valor;
    }
    
    private void completarInventarioJoin() {
    	
    	List<CatOltsEntity> oltsInventario = inventario.getOlts();
        ArrayList<CompletableFuture<GenericResponseDto>> regionSegmentOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
       
        Integer maxOnts = (oltsInventario.size() / 42) + 1;

        maxOnts = (oltsInventario.size() / 42) + 1;

        for (int i = 0; i < oltsInventario.size(); i += maxOnts) {
            Integer limMax = i + maxOnts;
            if (limMax >= oltsInventario.size()) {
                limMax = oltsInventario.size();
            }
            List<CatOltsEntity> listSegment = new ArrayList<CatOltsEntity>(
                    oltsInventario.subList(i, limMax));

            CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.completarEstatus(listSegment);
            regionSegmentOlts.add(executeProcess);
        }

        CompletableFuture.allOf(regionSegmentOlts.toArray(new CompletableFuture[regionSegmentOlts.size()])).join();

    }
    
    
    
}