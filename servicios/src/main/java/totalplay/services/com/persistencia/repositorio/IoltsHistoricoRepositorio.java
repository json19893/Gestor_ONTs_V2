package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import totalplay.services.com.persistencia.entidad.oltsHistorico;

public interface IoltsHistoricoRepositorio extends MongoRepository<oltsHistorico, String>{
	
	@Query(value="{'nombre': ?0}")
	oltsHistorico getNombre(String nombre);

}
