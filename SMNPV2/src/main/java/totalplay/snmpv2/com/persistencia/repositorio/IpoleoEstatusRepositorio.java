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
			,"{  $match: { $expr:  { $cond: [ ?2, {$eq: ['$id_olt',?3]}, '']}} }"
			,"{$out: ?1 }"
					})
	void outToAux(@Param("idEjecucion") String idEjecucion, @Param("tabla") String tabla, @Param("match") boolean match,  @Param("olt") Integer olt);
	
	@Aggregation(pipeline = { 
			"{$match:{id_ejecucion:?0}}"
			,"{$out: 'auxiliar_estatus'}"
					})
	void outToAuxUpdate(@Param("idEjecucion") String idEjecucion);
}
