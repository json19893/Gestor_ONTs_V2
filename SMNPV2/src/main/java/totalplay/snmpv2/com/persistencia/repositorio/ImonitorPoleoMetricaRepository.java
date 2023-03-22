package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoOltMetricaEntity;

public interface ImonitorPoleoMetricaRepository extends MongoRepository<MonitorPoleoMetricaEntity, String> {
	
	MonitorPoleoMetricaEntity findFirstByOrderByIdDesc();
	
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoMetricaEntity  getMonitorMetrica(@Param("id") String id);
}
