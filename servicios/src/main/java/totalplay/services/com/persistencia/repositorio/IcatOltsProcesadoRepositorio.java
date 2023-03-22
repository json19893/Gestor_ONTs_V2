package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.services.com.persistencia.entidad.catOtsProcesadoEntidad;

public interface IcatOltsProcesadoRepositorio extends MongoRepository<catOtsProcesadoEntidad, String> {

}
