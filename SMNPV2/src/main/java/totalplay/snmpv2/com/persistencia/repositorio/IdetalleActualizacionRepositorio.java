package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.detalleActualizacionesEntidad;

@Repository
public interface IdetalleActualizacionRepositorio extends MongoRepository<detalleActualizacionesEntidad, String> {

}