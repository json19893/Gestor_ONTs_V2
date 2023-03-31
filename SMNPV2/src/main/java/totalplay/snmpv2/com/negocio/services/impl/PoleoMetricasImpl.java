package totalplay.snmpv2.com.negocio.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.*;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;
import totalplay.snmpv2.com.persistencia.entidades.*;
import totalplay.snmpv2.com.persistencia.repositorio.*;

import org.springframework.scheduling.annotation.Async;

@Slf4j
@Service
public class PoleoMetricasImpl extends Constantes implements IpoleoMetricasService {

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
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, "", false);
                            break;
                        case 2:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, "", false);
                            break;
                        case 3:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, "", false);
                            break;
                        case 4:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, "", false);
                            break;
                        case 5:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, "", false);
                            break;
                        case 6:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, "", false);
                            break;
                        case 7:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, "", false);
                            break;
                        case 8:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, "", false);
                            break;
                        case 9:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, "", false);
                            break;
                        case 10:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, "", false);
                            break;
                        case 11:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, "", false);
                            break;
                        case 12:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, "", false);
                            break;
                        case 13:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, "", false);
                            break;
                        case 14:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, "", false);
                            break;
                        case 15:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, "", false);
                            break;
                        case 16:
                            metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, "", false);
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

    @Override
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
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 1, idOlt, PoleosEstatusEntity.class, false, oid, errorOlt);
                                break;
                            case 2:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 2, idOlt, PoleosLastDownCauseEntity.class, false, oid, errorOlt);
                                break;
                            case 3:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 3, idOlt, PoleosLastUpTimeEntity.class, false, oid, errorOlt);
                                break;
                            case 4:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 4, idOlt, PoleosLastDownTimeEntity.class, false, oid, errorOlt);
                                break;
                            case 5:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 5, idOlt, PoleosUpBytesEntity.class, false, oid, errorOlt);
                                break;
                            case 6:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 6, idOlt, PoleosDownBytesEntity.class, false, oid, errorOlt);
                                break;
                            case 7:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 7, idOlt, PoleosTimeOutEntity.class, false, oid, errorOlt);
                                break;
                            case 8:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 8, idOlt, PoleosUpPacketsEntity.class, false, oid, errorOlt);
                                break;
                            case 9:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 9, idOlt, PoleosDownPacketsEntity.class, false, oid, errorOlt);
                                break;
                            case 10:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 10, idOlt, PoleosDropUpPacketsEntity.class, false, oid, errorOlt);
                                break;
                            case 11:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 11, idOlt, PoleosDropDownPacketsEntity.class, false, oid, errorOlt);
                                break;
                            case 12:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 12, idOlt, PoleosCpuEntity.class, false, oid, errorOlt);
                                break;
                            case 13:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 13, idOlt, PoleosMemoryEntity.class, false, oid, errorOlt);
                                break;
                            case 14:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 14, idOlt, PoleosAliasEntity.class, false, oid, errorOlt);
                                break;
                            case 15:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 15, idOlt, PoleosProfNameEntity.class, false, oid, errorOlt);
                                break;
                            case 16:
                                metrica = genericMetrics.poleo(configPoleo, idMonitorPoleo, 16, idOlt, PoleosFrameSlotPortEntity.class, false, oid, errorOlt);
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
    public PostMetricaResponse getPoleoOntMetrica(RequestPostMetrica request) {
        PostMetricaResponse response = new PostMetricaResponse();
        response.setCod(1);
        response.setSms("");

        //String ruta = "/home/implementacion/ecosistema/comandos/";
        String ruta;

        Utils utilerias;

        final int RUN_STATUS = 1;
        String OID_RUN_STATUS = "";
        String num_serie = request.getNumero_serie();
        Integer idMetrica = new Integer(1);

        InventarioOntsEntity ont;
        CatOltsEntity olt;

        ont = inventario.getOntBySerialNumber(num_serie);

        //Supuesto: se recupero el id: 60;
        if (ont == null) {
            //Retorna

        }

        if (ont.getEstatus().equals("0")) {
            //Retorna
        }

        olt = catOltRepository.getOlt(ont.getId_olt());

        Query query = new Query();
        query.addCriteria(Criteria.where("id_metrica").is(RUN_STATUS));
        ConfiguracionMetricaEntity configMetrica = mongoTemplate.findOne(query, ConfiguracionMetricaEntity.class, "tb_configuracion_metricas");

        if (configMetrica.isActivo()) {
            if (ont.getTecnologia().equalsIgnoreCase("HUAWEI")) {
                configMetrica.getHUAWEI();
                OID_RUN_STATUS = ".1.3.6.1.4.1.2011.6.128.1.1.2.46.1.15.";
            }

            if (ont.getTecnologia().equalsIgnoreCase("ZTE")) {
                configMetrica.getZTE().getOid();
            }

            if (ont.getTecnologia().equalsIgnoreCase("FH")) {
                configMetrica.getFH();
            }
        } else {
            // return: No esta activada la metrica para este dispositivo
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
        configuracionDto configuracion = utilerias.getConfiguracion(out.getMappedResults());

        String comando = SNMP_GET + RETRIES_COMAD + RETRIES_VALUE + TIME_OUT_COMAND + TIME_OUT_VALUE
                + SPACE + configuracion.getVersion() + USER_NAME + configuracion.getUserName() + LEVEL
                + configuracion.getLevel() + PROTOCOL_ENCR + configuracion.getProtEn() + PASSPHRASE
                + configuracion.getPassword() + PROTOCOL_PRIV + configuracion.getProtPriv()
                + PROTOCOL_PHRASE + configuracion.getPhrase() + SPACE + IR + olt.getIp();

        ruta = "/home/ubuntu/bash/comandos/";

        //Crea el string para consultar la metrica
        HUAWEI_METRICAS METRICA = HUAWEI_METRICAS.SPLIT_RUN_STATUS_CADENA_HUAWEI;
        switch (METRICA) {
            case SPLIT_RUN_STATUS_CADENA_HUAWEI:
                String OIDs = Constantes.OID_LASTDOWNCAUSE_HUAWEI;
                System.out.println("OIDs: " + OIDs);
                break;
            case OID_LASTDOWNCAUSE_HUAWEI:
                System.out.println("metrica 2");
                break;
        }

        String OID = OID_RUN_STATUS + ont.getOid();
        String execute = comando + SPACE + OID;


        System.out.println(execute);


        try {
            BufferedReader buffer;
            EjecucionDto ejecucionDto = utilerias.execBash(execute, ruta);
            ejecucionDto.getProceso().waitFor();

            if (ejecucionDto.getProceso().exitValue() != 0) {
                return response;
            }
            InputStream inputStream = ejecucionDto.getProceso().getInputStream();
            System.out.println("Informacion: " + ejecucionDto.getProceso().exitValue());

            buffer = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = "";

            while ((readLine = buffer.readLine()) != null) {
                System.out.println(readLine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    enum HUAWEI_METRICAS {
        SPLIT_RUN_STATUS_CADENA_HUAWEI,
        OID_LASTDOWNCAUSE_HUAWEI
    }
}