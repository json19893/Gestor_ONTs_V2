package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.MonitorActualizacionEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.monitorPoleoNCEEntidad;


public interface ImonitorPoleoNCERepository extends MongoRepository<monitorPoleoNCEEntidad, String> {

	MonitorPoleoManualEntity findFirstByOrderByIdDesc();
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoManualEntity  getMonitorPoleo(@Param("id") String id);

	@Aggregation(pipeline = {"{\r\n" + //
			"    '$sort': {\r\n" + //
			"      '_id': -1\r\n" + //
			"    }\r\n" + //
			"  }"," {\r\n" + //
			"    '$limit': 1\r\n" + //
			"  }"})
	MonitorPoleoManualEntity  getUltimoDescubrimiento();
}
