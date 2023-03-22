package totalplay.login.com.persistencia.repositorio;



import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.stereotype.Repository;

import totalplay.login.com.persistencia.entidad.usuariosEntity;


@Repository
public interface IUsuariosPermitidosRepository extends MongoRepository<usuariosEntity, String> {
	long countByNombreUsuario(String nombreUsuario);
	usuariosEntity findByNombreUsuario(String nombreUsuario);



}
