package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.monitor.snmp.com.persistencia.entidad.tblMonitoreoEjecucionEntidad;

public interface ItblMonitoreoEjecucionRepositorio extends MongoRepository<tblMonitoreoEjecucionEntidad, String> {

}
