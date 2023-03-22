package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.MonitorActualizacionEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoMetricaEntity;




public interface ImonitorActualizacionEstatusRepository extends MongoRepository<MonitorActualizacionEstatusEntity, String> {

	MonitorActualizacionEstatusEntity findFirstByOrderByIdDesc();
	
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorActualizacionEstatusEntity  getMonitorEstatus(@Param("id") String id);
}
