package totalplay.services.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import totalplay.services.com.persistencia.entidad.InventarioOntsRespNCEEntity;

@Repository
public interface IinventarioOntsRespNCERepository extends MongoRepository<InventarioOntsRespNCEEntity, String> {

	@Query(value="{'numero_serie': ?0}")
	InventarioOntsRespNCEEntity getONT(String numSerie);

	@Aggregation(pipeline = {"{'$match':{'$and':[{'id_olt':?0},{'uid':?1}]}},{'frame':?2},{'port':?3},{'slot':?4}"})
	List<InventarioOntsRespNCEEntity> getOntsRespaldo(
			@Param("idOlt") Integer idOlt,
			@Param("uid") String uid,
			@Param("frame") Integer frame,
			@Param("port") Integer port,
			@Param("slot") Integer slot);
}




