package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import totalplay.monitor.snmp.com.persistencia.entidad.usuariosEntidad;



public interface IusuariosRepositorio extends MongoRepository<usuariosEntidad, String> {
	
	@Query(value = "{'usuario': ?0, 'password': ?1}")
	usuariosEntidad getUsuario(String usuario, String password);
}
