package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import totalplay.monitor.snmp.com.persistencia.entidad.TotalesByTecnologiaEntidad;
@Repository
public interface ITotalesByTecnologiaRepository extends MongoRepository<TotalesByTecnologiaEntidad, String> {
    //Busca el objeto en la base
    @Query("{tipo: ?0}")
    TotalesByTecnologiaEntidad getEntity(String tipo);
}
