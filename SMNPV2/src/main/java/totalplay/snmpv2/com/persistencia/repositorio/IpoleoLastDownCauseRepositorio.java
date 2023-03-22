package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosLastDownCauseEntity;


@Repository
public interface IpoleoLastDownCauseRepositorio extends MongoRepository<PoleosLastDownCauseEntity, String> {


	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: ?1 }"
					})
	void outToAux(@Param("idEjecucion") String idEjecucion, @Param("tabla") String tabla);
}
