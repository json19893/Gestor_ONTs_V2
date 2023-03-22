package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.MonitorActualizacionEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoEntity;


public interface ImonitorPoleoRepository extends MongoRepository<MonitorPoleoEntity, String> {

	MonitorPoleoEntity findFirstByOrderByIdDesc();
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoEntity  getMonitorPoleo(@Param("id") String id);
}
