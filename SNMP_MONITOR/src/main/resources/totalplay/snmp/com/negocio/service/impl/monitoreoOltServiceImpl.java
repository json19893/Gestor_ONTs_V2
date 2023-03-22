package totalplay.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.snmp.com.negocio.service.ImonitoreoOltService;
import totalplay.snmp.com.negocio.service.ImonitoreoRegionService;
import totalplay.snmp.com.persistencia.entidades.monitoreoOltEntidad;
import totalplay.snmp.com.persistencia.entidades.monitoreoRegionEntidad;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoOltRepositorio;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoRegionRepositorio;

@Service
public class monitoreoOltServiceImpl implements ImonitoreoOltService {

	@Autowired
	ImonitoreoOltRepositorio monitoreoOlt;

	@Override
	public String putMonitorOlt(String idRegion, String descripcion, String fechaInicio, Integer idEstatus, String id_olt) {
		// TODO Auto-generated method stub
		String response = null;
		try {
			monitoreoOltEntidad monitoreo = new monitoreoOltEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin("");
			monitoreo.setId_estatus(idEstatus);
			monitoreo.setFecha_inicio(fechaInicio);
			monitoreo.setId_region(idRegion);
			monitoreo.setId_olt(id_olt);
			response = monitoreoOlt.save(monitoreo).getId_registro_olt();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;
	}

	@Override
	public Integer updateMonitorOlt(String idRegion, String idIdRegistro, String descripcion, String fechaInicio,
			String fechaFin, Integer idEstatus, String id_olt) {
		Integer response = 0;
		try {
			monitoreoOltEntidad monitoreo = new monitoreoOltEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin(fechaFin);
			monitoreo.setId_estatus(idEstatus);
			monitoreo.setId_registro_olt(idIdRegistro);
			monitoreo.setFecha_inicio(fechaInicio);
			monitoreo.setId_region(idRegion);
			monitoreo.setId_olt(id_olt);
			monitoreoOlt.save(monitoreo); 
		} catch (Exception e) {
			response = 1;
		}
		return response;
	}

}
