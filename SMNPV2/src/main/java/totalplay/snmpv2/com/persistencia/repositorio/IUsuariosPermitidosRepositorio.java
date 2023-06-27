package totalplay.snmpv2.com.persistencia.repositorio;


import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.UsuariosPermitidosEntidad;

@Repository
public interface IUsuariosPermitidosRepositorio extends MongoRepository<UsuariosPermitidosEntidad, String> {	
	@Aggregation(pipeline = {
			"{$match:{ nombreUsuario: ?0 } }"
			})
	UsuariosPermitidosEntidad getUsuario(@Param("usuarios") String usuario);
	
	@Aggregation(pipeline = {
			  "{$set:{descubrimientos:[]}}\n"
			, "{$out:\"tb_usuarios_permitidos\"}"
			})
	void cleanDescubrimietosUsuarios();
	
}	