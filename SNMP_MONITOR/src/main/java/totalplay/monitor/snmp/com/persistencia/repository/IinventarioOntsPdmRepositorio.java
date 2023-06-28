package totalplay.monitor.snmp.com.persistencia.repository;


import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsPdmEntidad;

@Repository
public interface IinventarioOntsPdmRepositorio extends MongoRepository<inventarioOntsPdmEntidad, String> {
	@Query(value = " {'tipo':'E'}",count = true)
	Integer finOntsByTotalE();
	
	@Query(value = " {}",count = true)
	Integer finOntsByTotalT();
	
	@Query(value = " {'id_olt': ?0 }",count = true)
	Integer finOntsByTotalOlt(@Param("idOlt") Integer idOlt);
	@Query(value = " {'id_olt': ?0,'tipo':?1 }",count = true)
	Integer finOntsByTotalOltByTipo(@Param("idOlt") Integer idOlt,@Param("tipo") String tipo);

	@Query(value = " {'id_olt': ?0,'sa':true }",count = true)
	Integer finOntsByTotalOltByTipoSa(@Param("idOlt") Integer idOlt);

	@Query(value = " {'id_olt': ?0, 'estatus': ?1 }",count = true)
	Integer finOntsByTotalEstatus(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus);
	@Query(value = " {'id_olt': ?0, 'estatus': ?1 ,'tipo':?2}",count = true)
	Integer finOntsByTotalEstatusByTipo(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus,@Param("tipo") String tipo);

	@Query(value = " {'id_olt': ?0, 'estatus': ?1 ,'sa':true}",count = true)
	Integer finOntsByTotalEstatusByTipoSa(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus);
	
	@Aggregation(pipeline = { "{'$match':{'numero_serie':?0}}" })
	inventarioOntsPdmEntidad getOntBySerie(@Param("serie") String serie);
	
	@Query("{numero_serie: ?0}")
	inventarioOntsPdmEntidad finOntSerie(String numSerie);
	
}