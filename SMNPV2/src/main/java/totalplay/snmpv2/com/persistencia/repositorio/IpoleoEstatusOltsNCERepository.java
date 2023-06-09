package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.PoleoEstatusOltsNCEEntity;

public interface IpoleoEstatusOltsNCERepository   extends MongoRepository<PoleoEstatusOltsNCEEntity, String> {
	
	
	@Aggregation(pipeline = { 
			  "{\n"
			+ "		\"$lookup\":{\n"
			+ "			from: \"tb_inventario_onts\",\n"
			+ "			localField:\"index\",\n"
			+ "			foreignField:\"index\",\n"
			+ "			as: \"ont\",\n"
			+ "		}\n"
			+ "}\n"
			, " {$unwind: \"$ont\" } \n"
			, " {\n"
			+ "        $set:{\n"
			+ "            \"ont.estatus\":\"$estatus\",\n"
			+ "			   \"ont.fecha_modificacion\":{$dateSubtract:{ startDate: new Date(), unit: \"hour\", amount: 6}}"
			+ "        }\n"
			+ "}\n"
			, "{ $replaceRoot: { newRoot: \"$ont\" } }"
			, "{ $merge: { into: \"tb_inventario_onts\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
	})
	void getUpdateOnts();
}
