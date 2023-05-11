package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioGetOltsByRegionEntidad;

@Repository
public interface IEnvoltorioGetOltsByRegionRepository extends MongoRepository<EnvoltorioGetOltsByRegionEntidad, String> {
}
