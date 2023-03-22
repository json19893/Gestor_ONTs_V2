package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosEstatusEntity;



@Repository
public interface IpoleoEstatusRepositorio extends MongoRepository<PoleosEstatusEntity, String> {

	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: 'tb_status_onts'}"
					})
	void outToAux(@Param("idEjecucion") String idEjecucion);
	
	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: 'auxiliar_estatus'}"
					})
	void outToAuxUpdate(@Param("idEjecucion") String idEjecucion);
}
