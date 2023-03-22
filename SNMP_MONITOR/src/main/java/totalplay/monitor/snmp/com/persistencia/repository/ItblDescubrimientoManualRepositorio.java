package totalplay.monitor.snmp.com.persistencia.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import totalplay.monitor.snmp.com.persistencia.entidad.estatusPoleoManualEntidad;



@Repository
public interface ItblDescubrimientoManualRepositorio extends MongoRepository<estatusPoleoManualEntidad, String> {
	
	@Query(value="{ 'fecha': ?0}")
	List<estatusPoleoManualEntidad>  getDetalle( @Param("fecha")String fecha);
}
