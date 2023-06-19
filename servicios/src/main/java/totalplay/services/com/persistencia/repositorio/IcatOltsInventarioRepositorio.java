package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import totalplay.services.com.persistencia.entidad.catOltsInventarioEntidad;

public interface IcatOltsInventarioRepositorio extends MongoRepository<catOltsInventarioEntidad, String> {
	
	@Query(value="{'ip': ?0}")
	catOltsInventarioEntidad getIp(String ip);

}
