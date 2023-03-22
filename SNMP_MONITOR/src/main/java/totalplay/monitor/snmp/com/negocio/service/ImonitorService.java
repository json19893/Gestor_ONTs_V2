package totalplay.monitor.snmp.com.negocio.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import totalplay.monitor.snmp.com.negocio.dto.catRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;

import totalplay.monitor.snmp.com.negocio.dto.responseMonitoreo;
import totalplay.monitor.snmp.com.negocio.dto.responseRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.tbHistoricoDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOltsDto;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

public interface ImonitorService {

	responseRegionDto getOltsByRegion(Integer idRegion, String tipo, boolean onlyHeaders) throws Exception;

	List<catRegionDto> getRegion() throws Exception;

	List<inventarioOntsEntidad> getOntsByOlts(Integer idOlt, Integer estatus, String tipo) throws Exception;

	List<inventarioOntsEntidad> finOntsByIdAll(Integer idOlt, String tipo) throws Exception;

	List<tbHistoricoDto> getHistoricoCambios(Integer idOlt, String tipo) throws Exception;
	
	responseMonitoreo getDatosMonitoreo();

	List<datosRegionDto> getTotalesByTecnologia(String tipo) throws Exception;

	totalesActivoDto getTotalesActivo(String tipo) throws Exception;

	totalesOltsDto getTotalesByOlt(Integer idOlt, String tipo ) throws Exception;
	
	CompletableFuture<datosRegionDto> getConteo(Integer idRegion,String region) throws Exception;

	
}
