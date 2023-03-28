package totalplay.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.snmp.com.negocio.service.ImonitoreoEjecucionService;
import totalplay.snmp.com.negocio.service.ImonitoreoOltService;
import totalplay.snmp.com.negocio.service.ImonitoreoRegionService;
import totalplay.snmp.com.persistencia.entidades.monitoreoEjecucionEntidad;
import totalplay.snmp.com.persistencia.entidades.monitoreoOltEntidad;
import totalplay.snmp.com.persistencia.entidades.monitoreoRegionEntidad;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoEjecucionRepositorio;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoOltRepositorio;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoRegionRepositorio;

@Service
public class monitoreoEjecucionServiceImpl implements ImonitoreoEjecucionService {

	@Autowired
	ImonitoreoEjecucionRepositorio monitoreoEjecucion;

	@Override
	public String putMonitorEjecucion(String descripcion, String fechaInicio, Integer idEstatus) {
		// TODO Auto-generated method stub
		String response = null;
		try {
			monitoreoEjecucionEntidad monitoreo = new monitoreoEjecucionEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin("");
			monitoreo.setEstatus(idEstatus);
			monitoreo.setFecha_inicio(fechaInicio);
			response = monitoreoEjecucion.save(monitoreo).getId();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;
	}

	@Override
	public Integer updateMonitorEjecucion(String idIdRegistro, String descripcion, String fechaInicio,
			String fechaFin, Integer idEstatus) {
		Integer response = 0;
		try {
			monitoreoEjecucionEntidad monitoreo = new monitoreoEjecucionEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin(fechaFin);
			monitoreo.setEstatus(idEstatus);
			monitoreo.setId(idIdRegistro);
			monitoreo.setFecha_inicio(fechaInicio);
			
			monitoreoEjecucion.save(monitoreo).getId();
		} catch (Exception e) {
			response = 1;
		}
		return response;
	}
	
	@Override
	public String getLastId(){
		monitoreoEjecucionEntidad monitoreo = monitoreoEjecucion.findFirstByOrderByIdDesc();		
		return monitoreo.getId();
	}
}
