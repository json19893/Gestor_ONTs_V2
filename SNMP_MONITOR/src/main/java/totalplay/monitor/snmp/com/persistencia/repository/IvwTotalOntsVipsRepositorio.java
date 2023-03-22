package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEmpresarialesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsVipsEntidad;


public interface IvwTotalOntsVipsRepositorio extends MongoRepository<vwTotalOntsVipsEntidad, String> {
	@Aggregation(pipeline = { "{'$match':{'id_region': ?0 } }" })
	List<vwTotalOntsVipsEntidad> findByIdRegion(@Param("idRegion") Integer idRegion);
	
	@Aggregation(pipeline = { "{'$match':{'ip': ?0 } }" })
	List<vwTotalOntsVipsEntidad> findByIp(@Param("ip") String ip);	
	
	@Aggregation(pipeline = { "{'$match':{'nombre': ?0 } }" })
	List<vwTotalOntsVipsEntidad> findByNombre(@Param("nombre") String nombre);	
}
