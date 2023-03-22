package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoEjecucionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoOltEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoRegionEntidad;



@Repository
public interface ImonitoreoRegionRepositorio extends MongoRepository<monitoreoRegionEntidad, String> {
}
