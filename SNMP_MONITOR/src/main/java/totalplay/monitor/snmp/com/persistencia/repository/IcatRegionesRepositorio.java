package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.monitor.snmp.com.persistencia.entidad.catRegionEntidad;

public interface IcatRegionesRepositorio extends MongoRepository<catRegionEntidad, String> {

}
