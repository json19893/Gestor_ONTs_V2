package totalplay.monitor.snmp.com.persistencia.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasManualEntity;

public interface IdiferenciasManualRepository extends MongoRepository<DiferenciasManualEntity, String> {
	
	@Aggregation(pipeline = { "{'$match': {'$and': [{'id_olt': ?0}, {'numero_serie': ?1}]}}" })
	DiferenciasManualEntity getOntBySerieOlt(@Param("idOlt") Integer idOlt,@Param("serie") String serie);

    @Aggregation(pipeline = { "{'$match': {'$and': [ {'numero_serie': ?0}]}}" })
	List<DiferenciasManualEntity> getOntBySerie(@Param("serie") String serie);

	@Query(value="{'id_olt': ?0}",count=true)
	Integer findTotalCambios(@Param("idOlt") Integer idOlt );
}
