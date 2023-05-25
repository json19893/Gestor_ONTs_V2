package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;
import totalplay.snmpv2.com.persistencia.entidades.inventarioOntsErroneas;

public interface IinventarioOntsPdmRepository extends MongoRepository<InventarioOntsPdmEntity, String> {
	
	@Aggregation(pipeline = { 
	          "{\n"
	        + "		\"$lookup\":{\n"
	        + "			from: \"tb_inventario_onts_aux\",\n"
	        + "			localField:\"numero_serie\",\n"
	        + "			foreignField:\"numero_serie\",\n"
	        + "			as: \"onts\",\n"
	        + " 	}\n"
	        + "}\n"
	        , "{ $unwind: \"$onts\" }\n"
	        , "{ $match:{onts:{$ne:null}} }\n"
	        , "{$unset:[\"onts\"]}"
		})
	List<InventarioOntsPdmEntity> getInventario();
	
	@Query("{numero_serie: ?0}")
	InventarioOntsPdmEntity finOntSerie(String numSerie);
	
}
