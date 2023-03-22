package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;


public interface IconfiguracionMetricaRepository extends MongoRepository<ConfiguracionMetricaEntity, String> {

	@Query(value = "{'id_metrica': ?0, activo:true, bloque:?1 }", count = true)
	int getCountMetricasBloque(Integer idMetrica, Integer bloque);
	
	
	@Query(value = "{'id_metrica': ?0, poleo_general:true }", count = true)
	int getCountActive(Integer idMetrica);
	
	
	
	@Aggregation(pipeline = { "{'$match': {'$and':[{'id_metrica':?0}, {'id_configuracion':?1} ]  }}" })
	ConfiguracionMetricaEntity getMetrica(@Param("idMetrica") Integer idMetrica, @Param("idConfiguracion") Integer idConfiguracion);
	
}
