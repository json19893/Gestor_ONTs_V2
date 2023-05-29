package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasEntity;


public interface IdiferenciasRepository extends MongoRepository<DiferenciasEntity, String> {
	
	@Query("{numero_serie: ?0}")
	DiferenciasEntity getOntBySerialNumber(String numSerie);
	
}
