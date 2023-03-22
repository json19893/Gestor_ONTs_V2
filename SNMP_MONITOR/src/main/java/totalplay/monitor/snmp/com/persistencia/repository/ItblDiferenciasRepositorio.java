package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.persistencia.entidad.tblDiferenciasEntidad;

public interface ItblDiferenciasRepositorio extends MongoRepository<tblDiferenciasEntidad, String> {
	@Aggregation(pipeline = { "{'$match':{'id_olts':?0}}" })
	List<tblDiferenciasEntidad> finDife(@Param("idOlt") Integer idOlt);
	@Aggregation(pipeline = { "{'$match':{'numero_serie':?0}}" })
	List<tblDiferenciasEntidad> finNumeroSrie(@Param("numeroSerie") String numeroSerie);
}
