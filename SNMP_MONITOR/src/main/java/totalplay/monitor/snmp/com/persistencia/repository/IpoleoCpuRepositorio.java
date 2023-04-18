package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.poleosCpuEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosLastUpTimeEntidad;


@Repository
public interface IpoleoCpuRepositorio extends MongoRepository<poleosCpuEntidad, String> {
	//@Query(value="{'id_ejecucion': ?0, 'id_olt': ?1, 'oid': ?2, }")
	@Aggregation(pipeline = { 
			"{'$match':{'$and':[{'id_ejecucion': ?0},{'id_olt': ?1}]}}"
			,"{$sort:{_id:-1}}"
			,"{$limit:1}"
			})
	poleosCpuEntidad getMetrica(@Param("idEjecucion") String idEjecucion, @Param("idOlt") Integer idOlt, @Param("oid") String oid );
	
	@Aggregation(pipeline = { 
			"{'$match':{'$and':[{'id_ejecucion': ?0},{'index': ?1}]}}"
			,"{$sort:{_id:-1}}"
			,"{$limit:1}"
			})
	poleosCpuEntidad getMetricaByIndex(@Param("idEjecucion") String idEjecucion, @Param("index") String index );
}
