package totalplay.services.com.persistencia.repositorio;


import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.services.com.persistencia.entidad.catOltsEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.services.com.persistencia.entidad.inventarioOntsTempEntidad;



@Repository
public interface IinventarioOntsRepositorio extends MongoRepository<inventarioOntsEntidad, String> {
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'frame':?0},{'port':?1},{'slot':?2},{'uid':?3},{'id_olt':?4}]}}" })
	List <inventarioOntsEntidad> getNumeroSerie(
				@Param("frame") Integer frame, 
				@Param("port") Integer port,
				@Param("slot") Integer slot,
				@Param("uid") String uid,
				@Param("idOlt") Integer idOlt);
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'frame':?0},{'port':?1},{'slot':?2},{'uid':?3},{'id_olt':?4}]}}" })
	List <inventarioOntsEntidad>  getOnt(
				@Param("frame") Integer frame, 
				@Param("port") Integer port,
				@Param("slot") Integer slot,
				@Param("uid") String uid,
				@Param("idOlt") Integer idOlt);
	
	/*@Aggregation(pipeline = { "{'$match':{'$and':[{'numero_serie':'?0'}]}}" })
	inventarioOntsEntidad getONT(@Param("numSerie") String numSerie);*/
	
	@Query(value="{'numero_serie': ?0}")
	inventarioOntsEntidad getONT(String numSerie);
	
	@Query(value="{'id_olt': ?0}")
	List<inventarioOntsEntidad> getOntByOlt( Integer idOlt);


	
	@Aggregation(pipeline = { "{\r\n"
			+ "    '$match': {\r\n"
			+ "      'id_olt': ?0 \r\n"
			+ "    }\r\n"
			+ "  } "
			, "{\r\n"
			+ "    '$set': {\r\n"
			+ "      'estatus': 0 \r\n"
			+ "    }\r\n"
			+ "  } "
			, "{\r\n"
			+ "    '$merge': {\r\n"
			+ "      'into': 'tb_inventario_onts', \r\n"
			+ "      'on': '_id', \r\n"
			+ "      'whenMatched': 'replace', \r\n"
			+ "      'whenNotMatched': 'insert'\r\n"
			+ "    }\r\n"
			+ "  }" })
	void updateOnt(@Param("idOlt") Integer idOlt);
	

}
