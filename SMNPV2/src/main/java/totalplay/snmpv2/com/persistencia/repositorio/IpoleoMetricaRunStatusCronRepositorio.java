package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.PoleosRunEstatusEntity;


@Repository
public interface IpoleoMetricaRunStatusCronRepositorio extends MongoRepository<PoleosRunEstatusEntity, String> {

}
