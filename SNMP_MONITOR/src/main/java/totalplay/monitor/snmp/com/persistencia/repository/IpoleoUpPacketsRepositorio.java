package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.poleosCpuEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.poleosUpPacketsEntidad;


@Repository
public interface IpoleoUpPacketsRepositorio extends MongoRepository<poleosUpPacketsEntidad, String> {
	@Query(value="{'id_ejecucion': ?0, 'id_olt': ?1, 'oid': ?2, }")
	poleosUpPacketsEntidad getMetrica(@Param("idEjecucion") String idEjecucion, @Param("idOlt") Integer idOlt, @Param("oid") String oid );
}
