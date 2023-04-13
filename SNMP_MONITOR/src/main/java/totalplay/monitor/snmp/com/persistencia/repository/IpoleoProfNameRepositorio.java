package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.poleosCpuEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosProfNameEntidad;


@Repository
public interface IpoleoProfNameRepositorio extends MongoRepository<poleosProfNameEntidad, String> {
	//@Query(value="{'id_ejecucion': ?0, 'id_olt': ?1, 'oid': ?2, }")
	@Aggregation(pipeline = { 
			"{'$match':{'$and':[{'id_ejecucion': ?0},{'id_olt': ?1}]}}"
			,"{$sort:{_id:-1}}"
			,"{$limit:1}"
			})
	poleosProfNameEntidad getMetrica(@Param("idEjecucion") String idEjecucion, @Param("idOlt") Integer idOlt, @Param("oid") String oid );
}
