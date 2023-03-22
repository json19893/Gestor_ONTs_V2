package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosMemoryEntity;


@Repository
public interface IpoleoMemoryRepositorio extends MongoRepository<PoleosMemoryEntity, String> {
	
	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: 'auxiliar'}"
					})
	void outToAux(@Param("idEjecucion") String idEjecucion);
}
