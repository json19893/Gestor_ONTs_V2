package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.EstatusPoleoManualEntidad;

public interface ItblDescubrimientoManualRepositorio extends MongoRepository<EstatusPoleoManualEntidad, String> {
    @Query(value = "{'_id': ?0}")
	EstatusPoleoManualEntidad findByid(@Param("_id") String id);
	long countByEstatus(Integer estatus);
}
