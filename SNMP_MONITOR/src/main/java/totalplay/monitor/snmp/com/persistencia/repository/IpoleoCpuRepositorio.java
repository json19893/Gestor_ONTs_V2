package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.poleosCpuEntidad;


@Repository
public interface IpoleoCpuRepositorio extends MongoRepository<poleosCpuEntidad, String> {
	@Query(value="{'id_ejecucion': ?0, 'id_olt': ?1, 'oid': ?2, }")
	poleosCpuEntidad getMetrica(@Param("idEjecucion") String idEjecucion, @Param("idOlt") Integer idOlt, @Param("oid") String oid );
	
}
