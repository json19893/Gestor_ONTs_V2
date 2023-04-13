package totalplay.snmpv2.com.negocio.listeners;

import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.persistencia.entidades.PoleosEstatusEntity;

import java.util.List;

//Interface para definir listeners de eventos para las metricas
public interface MetricaCallback {
    void writterLogOnDiskMetrica(String path, configuracionDto logconfig, Object entidad, int status, Integer metrica, String comando) throws NoSuchMethodException, NoSuchFieldException;

}
