package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.services.com.persistencia.entidad.oltsNcePolearEntidad;



public interface IoltsNcePolearRepositorio extends MongoRepository<oltsNcePolearEntidad, String> {

}
