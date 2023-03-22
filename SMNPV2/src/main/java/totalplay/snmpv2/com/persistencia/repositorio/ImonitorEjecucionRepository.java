package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;

import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;




public interface ImonitorEjecucionRepository extends MongoRepository<MonitorEjecucionEntity, String> {
}
