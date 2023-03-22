package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import totalplay.monitor.snmp.com.persistencia.entidad.ConfiguracionMetricaEntity;

@Repository
public interface ITConfigMetricsRepository extends MongoRepository<ConfiguracionMetricaEntity, String> {

    @Aggregation(pipeline = { "{'$match':{'id_metrica':?0}}" })
    ConfiguracionMetricaEntity findByidMetrica(int idmetrica);
}

