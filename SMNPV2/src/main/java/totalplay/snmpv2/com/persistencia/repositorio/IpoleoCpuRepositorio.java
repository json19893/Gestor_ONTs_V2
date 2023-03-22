package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosCpuEntity;


@Repository
public interface IpoleoCpuRepositorio extends MongoRepository<PoleosCpuEntity, String> {
	
	@Aggregation(pipeline = { 
	"{$match:{id_ejecucion:?0}}"
	,"{$out: 'auxiliar'}"
			})
	void outToAux(@Param("idEjecucion") String idEjecucion);
}
