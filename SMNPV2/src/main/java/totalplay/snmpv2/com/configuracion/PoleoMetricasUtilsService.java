package totalplay.snmpv2.com.configuracion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.persistencia.entidades.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class PoleoMetricasUtilsService extends Constantes {

    @Autowired
    IGenericMetrics genericMetrics;

    /**
     * Esta funcion es una utileria generica para poleo de metricas
     *
     * @param configuracion:  Objeto que encapsula la configuracion para armar el comando snmp de forma dinamica en funcion de una olt.
     * @param idMonitorPoleo: Es el id generado del proceso de ejecuccion del poleo
     * @param idMetrica:      Es el id de la metrica que se quiere polear
     * @param idOlt:          Es el identificador de la olt de donde pertenece la ont a polear
     * @param saveErroneos:   Indica si se quiere generar la traza de errores
     * @param referencia:
     * @param error
     * @param manual:
     * @return metrica: Devuelve un objeto asyncrono que envuelve una GenericResponseDto.
     * @throws IOException: Son las excepciones cachadas por la ejecuccion del comando snmp en un entorno de ejecuccion linux
     */
    //@Async("taskExecutor2")
    public CompletableFuture<GenericResponseDto> dispatcherAsyncPoleoMetrica(
            configuracionDto configuracion, String idMonitorPoleo, int idMetrica,
            Integer idOlt, boolean saveErroneos,
            String referencia, boolean error, boolean manual) throws IOException, NoSuchFieldException, NoSuchMethodException {

        //Setteo de valores por defecto
        saveErroneos = saveErroneos ? true : false;
        referencia = referencia != "" ? referencia : "";
        error = error ? error : false;
        manual = manual ? manual : false;

        CompletableFuture<GenericResponseDto> metrica = new CompletableFuture<>();

        switch (idMetrica) {
            case RUN_STATUS:
                metrica = genericMetrics
                        .poleo(configuracion, idMonitorPoleo, RUN_STATUS, idOlt, PoleosEstatusEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case LAST_DOWN_CASE:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, LAST_DOWN_CASE, idOlt, PoleosLastDownCauseEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case LAST_UP_TIME:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, LAST_UP_TIME, idOlt, PoleosLastUpTimeEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case LAST_DOWN_TIME:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, LAST_DOWN_TIME, idOlt, PoleosLastDownTimeEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case UP_BYTES:
                metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, UP_BYTES, idOlt, PoleosUpBytesEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case DOWN_BYTES:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, DOWN_BYTES, idOlt, PoleosDownBytesEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case TIMEOUT:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, TIMEOUT, idOlt, PoleosTimeOutEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case UP_PACKETS:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, UP_PACKETS, idOlt, PoleosUpPacketsEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case DOWN_PACKETS:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, DOWN_PACKETS, idOlt, PoleosDownPacketsEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case DROP_UP_PACKETS:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, DROP_UP_PACKETS, idOlt, PoleosDropUpPacketsEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case DROP_DOWN_PACKETS:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, DROP_DOWN_PACKETS, idOlt, PoleosDropDownPacketsEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case CPU:
                metrica = genericMetrics.poleo(configuracion, idMonitorPoleo, CPU, idOlt, PoleosCpuEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case MEMORY:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, MEMORY, idOlt, PoleosMemoryEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case ALIAS_ONT:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, ALIAS_ONT, idOlt, PoleosAliasEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case PROF_NAME_ONT:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, PROF_NAME_ONT, idOlt, PoleosProfNameEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
            case FRAME_SLOT_PORT:
                metrica
                        = genericMetrics.poleo(configuracion, idMonitorPoleo, FRAME_SLOT_PORT, idOlt, PoleosFrameSlotPortEntity.class, saveErroneos, referencia, error, manual, false, false);
                break;
        }
        return metrica;
    }
}
