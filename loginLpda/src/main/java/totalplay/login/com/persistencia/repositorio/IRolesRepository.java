package totalplay.login.com.persistencia.repositorio;



import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import totalplay.login.com.persistencia.entidad.RolesEntity;
import totalplay.login.com.persistencia.entidad.usuariosEntity;


@Repository
public interface IRolesRepository extends MongoRepository<RolesEntity, String> {
	
	Optional<RolesEntity> findById(String id);

	


}
