package totalplay.monitor.snmp.com.negocio.service;


import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import totalplay.monitor.snmp.com.negocio.dto.*;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;


public interface IconsultaService {

	Map<String, Object> consultaNumeroSerie(String oid, String ip );
	responseFindDto getOlt(String tipo, String data, boolean ip);
	respuestaStatusDto cambiarEstatusOnt(List<requestEstatusDto> datos, String usuario);
	List<catOltsEntidad> obtenerOLTsActivas();
	responseFindOntDto getOnt(String tipo, String data, boolean serie);
	responseMetricasDto getMetrics(Integer idOlt, String oid);
	List<String> getArchivo(Integer archivo);
    responseDto actualizaOnt(String serie,Integer idOlt);

    List<DetalleActualizacionesOltsPojo> getDetalleActualizacionOlt() throws InvocationTargetException, IllegalAccessException;
}
