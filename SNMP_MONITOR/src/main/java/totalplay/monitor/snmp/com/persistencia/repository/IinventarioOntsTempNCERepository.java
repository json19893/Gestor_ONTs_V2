package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.InventarioOntsTmpNCEEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;


public interface IinventarioOntsTempNCERepository extends MongoRepository<InventarioOntsTmpNCEEntity, String> {
	
	@Query("{numero_serie: ?0}")
	inventarioOntsEntidad finOntSerie(String numSerie);
   

}
