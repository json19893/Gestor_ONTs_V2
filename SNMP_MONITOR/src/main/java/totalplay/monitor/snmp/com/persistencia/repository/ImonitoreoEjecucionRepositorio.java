package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.monitoreoEjecucionEntidad;



@Repository
public interface ImonitoreoEjecucionRepositorio extends MongoRepository<monitoreoEjecucionEntidad, String> {
	monitoreoEjecucionEntidad findFirstByOrderByIdDesc();
}
