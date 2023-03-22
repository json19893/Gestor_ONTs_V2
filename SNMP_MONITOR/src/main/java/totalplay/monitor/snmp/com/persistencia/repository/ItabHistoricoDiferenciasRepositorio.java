package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.tbHistoricoDiferenciasEntidad;

public interface ItabHistoricoDiferenciasRepositorio extends MongoRepository<tbHistoricoDiferenciasEntidad, String> {
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_olt':?0}]}}}" })
	List<tbHistoricoDiferenciasEntidad> findHistoricoCambios(@Param("idOlt") Integer idOlt);
	
	@Query(value="{'id_region': ?0}",count=true)
	Integer findTotalCambios(@Param("idRegion") Integer idRegion );
	
	@Query(value="{'id_olt': ?0}",count=true)
	Integer findTotalCambiosOlt(@Param("idOlt") Integer idOlt );
	
	
	@Query(value="{'id_olt': ?0, 'tipo':'E'}",count=true)
	Integer findTotalCambiosOltEmp(@Param("idOlt") Integer idOlt );
	
	@Query(value="{'id_olt': ?0, 'vip':1}",count=true)
	Integer findTotalCambiosOltVip(@Param("idOlt") Integer idOlt );
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_olt':?0}, {'tipo':'E'}]}}}" })
	List<tbHistoricoDiferenciasEntidad> findHistoricoCambiosEmp(@Param("idOlt") Integer idOlt);
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_olt':?0}, {'vip':1}]}}}" })
	List<tbHistoricoDiferenciasEntidad> findHistoricoCambiosVips(@Param("idOlt") Integer idOlt);

}
