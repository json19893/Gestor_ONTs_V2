package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoOltMetricaEntity;

public interface ImonitorPoleoOltMetricaRepository extends MongoRepository<MonitorPoleoOltMetricaEntity, String> {
	
	MonitorPoleoOltMetricaEntity findFirstByOrderByIdDesc();
	
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoOltMetricaEntity  getMonitorOlt(@Param("id") String id);
	
	@Aggregation(pipeline = {"{'$match':{$and:[{id_poleo: ?0}, {id_metrica:?1}, {id_olt:?2} ]} } "})
	MonitorPoleoOltMetricaEntity  getMonitorExist (@Param("idPoleo") String idPoleo, @Param("idMetrica") Integer idMetrica, @Param("idOlt") Integer idOlt );
	
}
