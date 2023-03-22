package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.MonitorPoleoOltMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.ParametrosGeneralesEntity;

public interface IparametrosGeneralesRepository extends MongoRepository<ParametrosGeneralesEntity, String> {
	
	
	
	@Aggregation(pipeline = {"{'$match':{id_configuracion: ?0} } "})
	ParametrosGeneralesEntity  getParametros(@Param("configuracion") Integer configuracion);
}
