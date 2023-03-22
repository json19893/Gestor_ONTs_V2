package totalplay.monitor.snmp.com.negocio.service;

import totalplay.monitor.snmp.com.negocio.dto.responseDto;

public interface IBlockMetricService {

    responseDto changeMetricBlock(int id_metrica, int id_bloque);

    responseDto getAllConfigMetrics();

    responseDto removeMetricBlock(int id_metrica, int id_bloque);
}
