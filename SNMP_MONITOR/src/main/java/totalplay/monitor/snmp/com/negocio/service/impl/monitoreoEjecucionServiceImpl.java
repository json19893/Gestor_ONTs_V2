package totalplay.monitor.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.monitor.snmp.com.negocio.service.ImonitoreoEjecucionService;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoEjecucionEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.ImonitoreoEjecucionRepositorio;


@Service
public class monitoreoEjecucionServiceImpl implements ImonitoreoEjecucionService {

	@Autowired
	ImonitoreoEjecucionRepositorio monitoreoEjecucion;


	@Override
	public String getLastId(){
		monitoreoEjecucionEntidad monitoreo = monitoreoEjecucion.findFirstByOrderByIdDesc();		
		return monitoreo.getId();
	}
}
