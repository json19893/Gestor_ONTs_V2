package totalplay.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import totalplay.snmp.com.negocio.service.ImonitoreoRegionService;
import totalplay.snmp.com.persistencia.entidades.monitoreoRegionEntidad;
import totalplay.snmp.com.persistencia.repositorio.ImonitoreoRegionRepositorio;

@Service
public class monitoreoRegionServiceImpl implements ImonitoreoRegionService {

	@Autowired
	ImonitoreoRegionRepositorio monitoreoRegion;

	@Override
	public String putMonitorRegion(Integer idRegion, String descripcion, String fechaInicio, Integer idEstatus) {
		// TODO Auto-generated method stub
		String response = null;
		try {
			monitoreoRegionEntidad monitoreo = new monitoreoRegionEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin("");
			monitoreo.setId_estatus(idEstatus);
			monitoreo.setFecha_inicio(fechaInicio);
			monitoreo.setId_region(idRegion);
			response = monitoreoRegion.save(monitoreo).getId_registro();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return response;
	}

	@Override
	public Integer updateMonitorRegion(Integer idRegion, String idIdRegistro, String descripcion, String fechaInicio,
			String fechaFin, Integer idEstatus) {
		Integer response = 0;
		try {
			monitoreoRegionEntidad monitoreo = new monitoreoRegionEntidad();
			monitoreo.setDescripcion(descripcion);
			monitoreo.setFecha_fin(fechaFin);
			monitoreo.setId_estatus(idEstatus);
			monitoreo.setId_registro(idIdRegistro);
			monitoreo.setFecha_inicio(fechaInicio);
			monitoreo.setId_region(idRegion);
			monitoreoRegion.save(monitoreo);
		} catch (Exception e) {
			response = 1;
		}
		return response;
	}

}
