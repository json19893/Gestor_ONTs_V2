package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.MonitorActualizacionEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoManualEntity;


public interface ImonitorPoleoManualRepository extends MongoRepository<MonitorPoleoManualEntity, String> {

	MonitorPoleoManualEntity findFirstByOrderByIdDesc();
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoManualEntity  getMonitorPoleo(@Param("id") String id);
}
