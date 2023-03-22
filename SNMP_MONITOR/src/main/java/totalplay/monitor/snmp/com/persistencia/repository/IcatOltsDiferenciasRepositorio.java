package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsDiferenciasEntidad;

public interface IcatOltsDiferenciasRepositorio extends MongoRepository<catOltsDiferenciasEntidad, String> {
	
}
