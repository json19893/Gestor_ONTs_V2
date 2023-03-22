package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosUpBytesEntity;

@Repository
public interface IpoleoUpBytesRepositorio extends MongoRepository<PoleosUpBytesEntity, String> {

	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: 'auxiliar'}"
					})
	void outToAux(@Param("idEjecucion") String idEjecucion);
}
