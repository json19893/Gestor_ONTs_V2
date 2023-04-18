package totalplay.snmpv2.com.negocio.services.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang.ArrayUtils;
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
import totalplay.snmpv2.com.persistencia.repositorio.*;
import org.springframework.scheduling.annotation.Async;
import totalplay.snmpv2.com.persistencia.repositorio.IauxiliarJoinEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IconfiguracionMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IfaltantesMetricasRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatConfiguracionEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoOltMetricaEntity;
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

        String fechaInicio = LocalDate.now().toString();
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
                        idMonitorOlt = monitorPoleoOltMetrica.save(new MonitorPoleoOltMetricaEntity(idOlt, Integer.valueOf(idMetrica), LocalDateTime.now().toString(), idMonitorPoleo)).getId();


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
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, "", false, false);
                            break;
                        case 2:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, "", false, false);
                            break;
                        case 3:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, "", false, false);
                            break;
                        case 4:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, "", false, false);
                            break;
                        case 5:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, "", false, false);
                            break;
                        case 6:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, "", false, false);
                            break;
                        case 7:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, "", false, false);
                            break;
                        case 8:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, "", false, false);
                            break;
                        case 9:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, "", false, false);
                            break;
                        case 10:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, "", false, false);
                            break;
                        case 11:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, "", false, false);
                            break;
                        case 12:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, "", false, false);
                            break;
                        case 13:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, "", false, false);
                            break;
                        case 14:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, "", false, false);
                            break;
                        case 15:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, "", false, false);
                            break;
                        case 16:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, "", false, false);
                            break;
                    }


                    MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
                    monitorPoleoOlt.setFecha_inicio(fechaInicio);
                    monitorPoleoOlt.setFecha_fin(LocalDateTime.now().toString());
                    monitorPoleoOlt.setError(metrica.get().getSms().equals("0") || metrica.get().getSms().equals("error") || metrica.get().getSms().equals("Sin metrica"));
                    monitorPoleoOlt.setResultado(metrica.get().getSms());

                    monitorPoleoOltMetrica.save(monitorPoleoOlt);

                } catch (Exception e) {
                    log.error("error:" + e);

                    if (!idMonitorOlt.equals("")) {
                        MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
                        monitorPoleoOlt.setFecha_inicio(fechaInicio);
                        monitorPoleoOlt.setFecha_fin(LocalDateTime.now().toString());
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
    public List<OntsConfiguracionDto> getOntsFaltantes(int idMetrica, String idEjecucion, boolean resultado, boolean empresariales, String tabla, int tipo, List<CatOltsEntity> olts) {

        List<OntsConfiguracionDto> faltantes = null;

        try {
            switch (idMetrica) {
                case 1:
                    poleoEstatus.outToAux(idEjecucion, tabla);
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
                    poleoFrameSlotPort.outToAux(idEjecucion, tabla);
                    break;
            }
        } catch (Exception e) {
            log.info(e.toString());
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
                System.out.println(e);
            }
            return faltantes;
        }
        return null;

    }

    @Async("taskExecutor")
    public CompletableFuture<String> getMetricaEmpresarialesByMetrica(List<OntsConfiguracionDto> ontsEmpresariales,
                                                                      String idMonitorPoleo, int idMetrica) {

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

                if (ont.isError() || ont.getPoleable().intValue() == 0)
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
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, oid, errorOlt, false);
                                break;
                            case 2:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, oid, errorOlt, false);
                                break;
                            case 3:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, oid, errorOlt, false);
                                break;
                            case 4:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, oid, errorOlt, false);
                                break;
                            case 5:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, oid, errorOlt, false);
                                break;
                            case 6:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, oid, errorOlt, false);
                                break;
                            case 7:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, oid, errorOlt, false);
                                break;
                            case 8:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, oid, errorOlt, false);
                                break;
                            case 9:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, oid, errorOlt, false);
                                break;
                            case 10:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, oid, errorOlt, false);
                                break;
                            case 11:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, oid, errorOlt, false);
                                break;
                            case 12:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, oid, errorOlt, false);
                                break;
                            case 13:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, oid, errorOlt, false);
                                break;
                            case 14:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, oid, errorOlt, false);
                                break;
                            case 15:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, oid, errorOlt, false);
                                break;
                            case 16:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, oid, errorOlt, false);
                                break;
                        }

                    } catch (Exception e) {
                        log.info(e.toString());
                    }
                }

            } catch (Exception e) {
                log.info(e.toString());
            }
        }

        return null;

    }

    @Override
    public void joinUpdateStatus(String idMonitorPoleo) {

        log.info("::::::::::::::::::::::::::::::::::::Inicia la actualizaciòn de estatus del inventario final");

        getOntsFaltantes(1, idMonitorPoleo, false, true, "auxiliar_estatus", 2, null);


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

            try {
                auxiliarJoinEstatus.sendToInventario();
            } catch (Exception e) {
                log.info(e.toString());
            }

        } catch (Exception e) {
            log.info(e.toString());
        }

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

            maxOnts = (oltsInventario.size() / 42) + 1;

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
            log.info(e.toString());
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
        logEventos = "[ " + util.getCurrentDateTime() + " ] " + " INFO " + " [Funcion a Ejecutar]:  PoleoMetricasImpl.getPoleoOntMetrica" + "\n";

        //System.out.println(logEventos);
        //Parametros de entrada:
        String num_serie = request.getNumero_serie();
        Integer idMetrica = request.getIdMetrica();
        String fechaInicio = LocalDate.now().toString();

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

        if (ont.getEstatus().equals("0")) {
            response.setCod(RESOURCE_NOT_AVAILABLE);
            response.setSms("Error: recurso no disponible");
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

        if (configMetrica.isActivo()) {
            if (ont.getTecnologia().equalsIgnoreCase("HUAWEI")) {
                OID_METRICA = configMetrica.getHUAWEI().getOid() + "." + ont.getOid();
            }

            if (ont.getTecnologia().equalsIgnoreCase("ZTE")) {
                OID_METRICA = configMetrica.getZTE().getOid() + "." + ont.getOid();
            }

            if (ont.getTecnologia().equalsIgnoreCase("FH")) {
                OID_METRICA = configMetrica.getFH().getOid() + "." + ont.getOid();
            }
        } else {
            //No esta activada la metrica para este dispositivo
            response.setCod(TECHNOLOGY_NOT_SUPPORTED);
            response.setSms("Error: Tecnologia no soportada por esta version");
            return response;
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


        final String BASE_COMMAND = SNMP_GET + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE
                + SPACE + configuracionPoleo.getVersion() + USER_NAME + configuracionPoleo.getUserName() + LEVEL
                + configuracionPoleo.getLevel() + PROTOCOL_ENCR + configuracionPoleo.getProtEn() + PASSPHRASE
                + configuracionPoleo.getPassword() + PROTOCOL_PRIV + configuracionPoleo.getProtPriv()
                + PROTOCOL_PHRASE + configuracionPoleo.getPhrase() + SPACE + IR + olt.getIp();

        Integer idOlt = olt.getId_olt();
        String oid = ont.getOid();
        String idMonitorPoleo = monitorPoleo.findFirstByOrderByIdDesc().getId();
        configuracionPoleo.setComando(BASE_COMMAND);
        configuracionPoleo.setIdConfiguracion(idConfiguracion);
        configuracionPoleo.setTecnologia(tecnologia);
        configuracionPoleo.setTrazaEventos(logEventos);

        String finalLogEventos = logEventos;

        //Escucha los eventos
        configuracionPoleo.setManejarResultadoComando((ruta, c, result, status, metrica, comando) -> {
            util.deleteLogFile(ruta);
            String copyString = finalLogEventos;
            String valor = "";
            try{

                valor = obtenerValor(metrica, result);
                log.info("################# ::"+valor);
            }catch (Exception e){
                e.printStackTrace();
            }

            String tmp = c.getTrazaEventos();
            if (status == 0) {
                tmp += "[ " + util.getCurrentDateTime() + " ] " + " INFO " + " [Comando Ejecutado]: " + comando + "\n";
                tmp += "[ " + util.getCurrentDateTime() + " ] " + " INFO " + " [Resultado Ejecuccion]: " + valor + "\n";
                c.setTrazaEventos(tmp);
            } else {
                tmp += "[ " + util.getCurrentDateTime() + " ] " + " ERROR " + " [Error en la ejecuccion del comando]: " + comando + "\n";
                c.setTrazaEventos(tmp);
            }
            tmp += "[ " + util.getCurrentDateTime() + " ] " + " INFO " + " [Guardando en Disco]: " + ruta + "\n";
            util.crearArchivos(ruta, c.getTrazaEventos());
        });


        //String resultado = configuracionPoleo.getManejarResultadoComando().getCommandResponse("10");
        //System.out.println(resultado);

        CompletableFuture<GenericResponseDto> metricaAsyncProcess = new CompletableFuture<>();
        boolean errorOlt = false;

        metricaAsyncProcess = poleoMetricasUtilsService
                .dispatcherAsyncPoleoMetrica(
                        configuracionPoleo,
                        idMonitorPoleo,
                        request.getIdMetrica(),
                        olt.getId_olt(),
                        false,
                        oid,
                        false,
                        false);

        MonitorPoleoOltMetricaEntity monitor;
        String idMonitorOlt = "";

        monitor = monitorPoleoOltMetrica.getMonitorExist(idMonitorPoleo, idMetrica, olt.getId_olt());
        if (monitor != null)
            idMonitorOlt = monitor.getId();
//        else
//            idMonitorOlt = monitorPoleoOltMetrica.save(new MonitorPoleoOltMetricaEntity(idOlt, Integer.valueOf(idMetrica), LocalDateTime.now().toString(), idMonitorPoleo)).getId();

        boolean isErrorAsyncProcess
                = metricaAsyncProcess.get().getSms().equals("0")
                || metricaAsyncProcess.get().getSms().equals("error")
                || metricaAsyncProcess.get().getSms().equals("Sin metrica");

        if (isErrorAsyncProcess) {
//            MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
//            monitorPoleoOlt.setFecha_inicio(fechaInicio);
//            monitorPoleoOlt.setFecha_fin(LocalDateTime.now().toString());
//            monitorPoleoOlt.setError(true);
//
//            monitorPoleoOltMetrica.save(monitorPoleoOlt);
            //Lanza una excepcion
            throw new RuntimeException("Error: No asigno el id process para el proceso de metrica");
        }

//        MonitorPoleoOltMetricaEntity monitorPoleoOlt = monitorPoleoOltMetrica.getMonitorOlt(idMonitorOlt);
//        monitorPoleoOlt.setFecha_inicio(fechaInicio);
//        monitorPoleoOlt.setFecha_fin(LocalDateTime.now().toString());
//        monitorPoleoOlt.setError(isErrorAsyncProcess);
//        monitorPoleoOlt.setResultado(metricaAsyncProcess.get().getSms());

        GenericResponseDto res = metricaAsyncProcess.get();
        res.setSms("Se ejecuto correctamente la metrica");
        res.setCod(0);
        return res;
    }

    private String obtenerValor(Integer metrica, Object result) {
        String valor = "";
        InventarioOntsEntity inv=new InventarioOntsEntity();
        switch (metrica) {
            case RUN_STATUS:
                PoleosEstatusEntity me = (PoleosEstatusEntity) result;
                valor = me.getValor();
                inv=  inventario.getOntByOid(me.getId_olt(), me.getOid());
                inv.setEstatus(
                me.getValor().equals("up(1)") ? 1: 
                me.getValor().equals("down(2)")? 2:
                me.getValor().equals("6")?1:
                me.getValor().equals("1")?1:
                me.getValor().equals("2")?2:
                me.getValor().equals("3")?3:2);
                break;
            case LAST_DOWN_CASE:
                PoleosLastDownCauseEntity me1 = (PoleosLastDownCauseEntity) result;
                valor = me1.getValor();
                inv=  inventario.getOntByOid(me1.getId_olt(), me1.getOid());
                inv.setDescripcionAlarma(me1.getValor());
                break;
            case LAST_UP_TIME:
                PoleosLastUpTimeEntity me2 = (PoleosLastUpTimeEntity) result;
                valor = me2.getValor();
                
                break;
            case LAST_DOWN_TIME:
                PoleosLastDownTimeEntity me3 = (PoleosLastDownTimeEntity) result;
                valor = me3.getValor();
                inv=  inventario.getOntByOid(me3.getId_olt(), me3.getOid());
                inv.setLastDownTime(me3.getValor());
                break;
            case UP_BYTES:
                PoleosUpBytesEntity me4 = (PoleosUpBytesEntity) result;
                valor = me4.getValor();
                break;
            case DOWN_BYTES:
                PoleosDownBytesEntity me5 = (PoleosDownBytesEntity) result;
                valor = me5.getValor();
                break;
            case TIMEOUT:
                PoleosTimeOutEntity me6 = (PoleosTimeOutEntity) result;
                valor = me6.getValor();
                break;
            case UP_PACKETS:
                PoleosUpPacketsEntity me7 = (PoleosUpPacketsEntity) result;
                valor = me7.getValor();
                break;
            case DOWN_PACKETS:
                PoleosDownPacketsEntity me8 = (PoleosDownPacketsEntity) result;
                valor = me8.getValor();
                break;
            case DROP_UP_PACKETS:
                PoleosDropUpPacketsEntity me9 = (PoleosDropUpPacketsEntity) result;
                valor = me9.getValor();
                break;
            case DROP_DOWN_PACKETS:
                PoleosDropDownPacketsEntity me10 = (PoleosDropDownPacketsEntity) result;
                valor = me10.getValor();
                break;
            case CPU:
                PoleosCpuEntity me11 = (PoleosCpuEntity) result;
                valor = me11.getValor();
                break;
            case MEMORY:
                PoleosMemoryEntity me12 = (PoleosMemoryEntity) result;
                valor = me12.getValor();
                break;
            case ALIAS_ONT:
                PoleosAliasEntity me13 = (PoleosAliasEntity) result;
                valor = me13.getValor();
                inv=  inventario.getOntByOid(me13.getId_olt(), me13.getOid());
                inv.setAlias(me13.getValor());
                break;
            case PROF_NAME_ONT:
                PoleosProfNameEntity me14 = (PoleosProfNameEntity) result;
                valor = me14.getValor();
                break;
            case FRAME_SLOT_PORT:
                PoleosFrameSlotPortEntity me15 = (PoleosFrameSlotPortEntity) result;
                valor = me15.getFrame()+"/"+me15.getSlot()+"/"+me15.getPort();
                inv.setFrame(me15.getFrame());
                inv.setSlot(me15.getSlot());
                inv.setPort(me15.getPort());
                break;
        }
        if (inv!=null){
         inventario.save(inv);
        }
        return valor;
    }
}