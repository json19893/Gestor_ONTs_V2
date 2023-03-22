package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOltsEntidad;

public interface IvwTotalRegionRepositorio extends MongoRepository<vwTotalOltsEntidad, Integer> {

}
