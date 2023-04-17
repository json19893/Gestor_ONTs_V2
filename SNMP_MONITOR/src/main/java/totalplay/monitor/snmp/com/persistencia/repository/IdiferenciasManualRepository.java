package totalplay.monitor.snmp.com.persistencia.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasManualEntity;

public interface IdiferenciasManualRepository extends MongoRepository<DiferenciasManualEntity, String> {
	
	
}
