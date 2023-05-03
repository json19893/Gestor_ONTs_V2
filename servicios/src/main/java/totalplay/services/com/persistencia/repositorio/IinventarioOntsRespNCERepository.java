package totalplay.services.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.services.com.persistencia.entidad.InventarioOntsRespNCEEntity;


public interface IinventarioOntsRespNCERepository extends MongoRepository<InventarioOntsRespNCEEntity, String> {
	
	@Query(value="{'numero_serie': ?0}")
	InventarioOntsRespNCEEntity getONT(String numSerie);
}




