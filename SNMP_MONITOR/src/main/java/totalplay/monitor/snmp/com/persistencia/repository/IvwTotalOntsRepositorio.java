package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;

public interface IvwTotalOntsRepositorio extends MongoRepository<vwTotalOntsEntidad, String> {
	@Aggregation(pipeline = { "{'$match':{'id_region': ?0 } }" })
	List<vwTotalOntsEntidad> findByIdRegion(@Param("idRegion") Integer idRegion);
	
	@Aggregation(pipeline = { "{'$match':{'ip': ?0 } }" })
	List<vwTotalOntsEntidad> findByIp(@Param("ip") String ip);
	
	@Aggregation(pipeline = { "{'$match':{'nombre': ?0 } }" })
	List<vwTotalOntsEntidad> findByNombre(@Param("nombre") String nombre);
	
}
