package totalplay.monitor.snmp.com.persistencia.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.catConfiguracionEntidad;







@Repository
public interface IcatConfiguracionRepositorio extends MongoRepository<catConfiguracionEntidad, String> {
	@Query(value="{'id_configuracion': ?0, 'fecha': ?1}")
	catConfiguracionEntidad  getIntentos(@Param("id_configuracion")Integer idConfiguracion, @Param("fecha")String fecha);
	
}
