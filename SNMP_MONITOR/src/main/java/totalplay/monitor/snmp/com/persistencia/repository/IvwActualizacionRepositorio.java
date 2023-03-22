package totalplay.monitor.snmp.com.persistencia.repository;



import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;

@Repository
public interface IvwActualizacionRepositorio extends MongoRepository<vwActualizacionEntidad, String> {


	@Aggregation(pipeline = {"{\n"
			+ "    '$match': {\n"
			+ "      '$and': [\n"
			+ "        {\n"
			+ "          'numero_serie': new RegExp('.*"+"?0"+".*') \n"
			+ "        } \n"
			+ "      ]\n"
			+ "    }\n"
			+ "  }" })
	List<vwActualizacionEntidad> findSerieByRegexT(@Param("regex") String regex);

	@Aggregation(pipeline = {"{\n"
			+ "    '$match': {\n"
			+ "      '$and': [\n"
			+ "        {\n"
			+ "          'numero_serie': new RegExp('.*"+"?0"+".*') \n"
			+ "        }, {\n"
			+ "          'tipo': 'E'\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    }\n"
			+ "  }" })
	List<vwActualizacionEntidad> findSerieByRegexE(@Param("regex") String regex);
	
	@Aggregation(pipeline = {"{\n"
			+ "    '$match': {\n"
			+ "      '$and': [\n"
			+ "        {\n"
			+ "          'numero_serie': ?0 \n"
			+ "        } \n"
			+ "      ]\n"
			+ "    }\n"
			+ "  }" })
	List<vwActualizacionEntidad> findSerieT(@Param("regex") String regex);
	
	@Aggregation(pipeline = {"{\n"
			+ "    '$match': {\n"
			+ "      '$and': [\n"
			+ "        {\n"
			+ "          'numero_serie': ?0 \n"
			+ "        }, {\n"
			+ "          'tipo': 'E'\n"
			+ "        }\n"
			+ "      ]\n"
			+ "    }\n"
			+ "  }" })
	List<vwActualizacionEntidad> findSerieE(@Param("regex") String regex);


}
	