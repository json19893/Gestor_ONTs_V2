package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.monitor.snmp.com.persistencia.entidad.DetalleActualizacionesOltsEntity;

import java.util.List;

public interface IDetalleActualizacionesOltsRepository extends MongoRepository<DetalleActualizacionesOltsEntity,String> {
    List<DetalleActualizacionesOltsEntity> findByIp(String ipOlt);
}
