package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.tblBitacoraEventosEntidad;





@Repository
public interface bitacoraEventosRepository  extends MongoRepository<tblBitacoraEventosEntidad, String>{

}
