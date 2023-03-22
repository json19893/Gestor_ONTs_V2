package totalplay.monitor.snmp.com.negocio.service;

import java.util.List;
import java.util.Map;

import totalplay.monitor.snmp.com.negocio.dto.requestEstatusDto;
import totalplay.monitor.snmp.com.negocio.dto.responseFindDto;
import totalplay.monitor.snmp.com.negocio.dto.responseFindOntDto;
import totalplay.monitor.snmp.com.negocio.dto.responseMetricasDto;
import totalplay.monitor.snmp.com.negocio.dto.respuestaCambioEstatusDto;
import totalplay.monitor.snmp.com.negocio.dto.respuestaStatusDto;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;

public interface IconsultaService {

	Map<String, Object> consultaNumeroSerie(String oid, String ip );
	responseFindDto getOlt(String tipo, String data, boolean ip);
	respuestaStatusDto cambiarEstatusOnt(List<requestEstatusDto> datos, String usuario);
	List<catOltsEntidad> obtenerOLTsActivas();
	responseFindOntDto getOnt(String tipo, String data, boolean serie);
	responseMetricasDto getMetrics(Integer idOlt, String oid);
}
