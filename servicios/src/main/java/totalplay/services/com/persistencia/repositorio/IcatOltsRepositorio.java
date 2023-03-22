package totalplay.services.com.persistencia.repositorio;


import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


import totalplay.services.com.persistencia.entidad.catOltsEntidad;


public interface IcatOltsRepositorio extends MongoRepository<catOltsEntidad, String> {
	@Query(value="{'ip': ?0}")
	catOltsEntidad getIp(String ip);	
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'tecnologia':'HUAWEI'},{estatus:0}]}}" })
	List<catOltsEntidad> getConfiguracionOlt();
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_olt':?0}]}}" })
	catOltsEntidad getOlt(@Param("idOlt") Integer idOlt);
	@Aggregation(pipeline = { "{\n"
			+ "    '$sort': {\n"
			+ "      'id_olt': -1\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$limit': 1\n"
			+ "  }" })
	catOltsEntidad getIdOltMAX();
	
}
