package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoEjecucionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoOltEntidad;



@Repository
public interface ImonitoreoOltRepositorio extends MongoRepository<monitoreoOltEntidad, String> {
	monitoreoOltEntidad findFirstByOrderByIdDesc();
}
