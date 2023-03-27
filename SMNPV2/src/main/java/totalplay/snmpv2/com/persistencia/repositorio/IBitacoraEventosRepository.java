package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.snmpv2.com.persistencia.entidades.BitacoraEventosEntity;
public interface IBitacoraEventosRepository   extends MongoRepository<BitacoraEventosEntity, String> {
    
}
