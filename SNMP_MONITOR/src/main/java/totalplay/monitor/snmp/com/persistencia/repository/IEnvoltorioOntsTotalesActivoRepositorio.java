package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
@Repository
public interface IEnvoltorioOntsTotalesActivoRepositorio extends MongoRepository<EnvoltorioOntsTotalesActivoEntidad, String> {

}
