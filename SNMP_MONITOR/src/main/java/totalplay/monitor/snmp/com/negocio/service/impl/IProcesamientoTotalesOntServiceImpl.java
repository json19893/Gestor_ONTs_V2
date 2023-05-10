package totalplay.monitor.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.negocio.service.IProcesamientoTotalesOntService;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;

//Esta clase contiene procesamiento batch
@Service
public class IProcesamientoTotalesOntServiceImpl implements IProcesamientoTotalesOntService {
    @Autowired
    IcatOltsRepositorio catOltsRepositorio;
    /**
     * Proceso batch para obtener el total de onts de la base de datos
     */
    @Override
    public void procesarTotalesOnts() {
        EnvoltorioOntsTotalesActivoEntidad envoltorio = new EnvoltorioOntsTotalesActivoEntidad();
        //Aqui manda a llamar al negocio:
    }
}
