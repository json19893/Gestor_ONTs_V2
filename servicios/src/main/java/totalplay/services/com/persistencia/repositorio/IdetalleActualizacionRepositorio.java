package totalplay.services.com.persistencia.repositorio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.services.com.persistencia.entidad.detalleActualizacionesEntidad;

@Repository
public interface IdetalleActualizacionRepositorio extends MongoRepository<detalleActualizacionesEntidad, String> {

}