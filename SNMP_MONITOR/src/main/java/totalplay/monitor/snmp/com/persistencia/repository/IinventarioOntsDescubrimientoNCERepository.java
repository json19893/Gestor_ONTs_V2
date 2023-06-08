package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.InventarioOntsDescubrimientoNCEEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;


public interface IinventarioOntsDescubrimientoNCERepository extends MongoRepository<InventarioOntsDescubrimientoNCEEntity, String> {
	
	@Query(value="{'id_ejecucion' : ?0}", delete = true)
	void deleteEjecucion(@Param("ejecucion") String ejecucion);
	
	@Query(value = " {'numero_serie': ?0, 'id_ejecucion': ?1 }")
	inventarioOntsEntidad findOnt(@Param("serie") String serie,@Param("ejecucion") String ejecucion);
	
}
