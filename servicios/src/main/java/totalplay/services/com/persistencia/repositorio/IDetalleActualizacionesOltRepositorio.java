package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.services.com.persistencia.entidad.detalleActualizacionesOltEntidad;





public interface IDetalleActualizacionesOltRepositorio extends MongoRepository<detalleActualizacionesOltEntidad, String> {

}
