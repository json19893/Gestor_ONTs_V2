package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsNCEEntity;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.query.Param;


public interface IcatOltsNCERepository extends MongoRepository<CatOltsNCEEntity, String> {
	
	@Aggregation(pipeline = { 
		        "{\n"
		      + "		\"$lookup\":{\n"
		      + "			from: \"cat_olts\",\n"
		      + "			localField: \"ip_olt\",\n"
		      + "            foreignField: \"ip\",\n"
		      + "			as: \"olts\",\n"
		      + "		}			\n"
		      + "}\n"
		      , "{ $match:{olts:{$eq:[]}} }\n"
		      , "{\n"
		      + "		\"$lookup\":{\n"
		      + "			from: \"cat_olts\",\n"
		      + "			localField: \"nombre_olt\",\n"
		      + "            foreignField: \"nombre\",\n"
		      + "			as: \"oltNombre\",\n"
		      + "		}			\n"
		      + "}\n"
		      , "{ $unwind:{ path:\"$oltNombre\",  preserveNullAndEmptyArrays: true } }\n"
		      , "{    \n"
		      + "	    $set:{\n"
		      + "	        oltNombre:{ \n"
		      + "	            $cond: [ \"$oltNombre\", \n"
		      + "        	            { $mergeObjects: [\"$oltNombre\",{ip:\"$ip_olt\"} ,{descripcion:\"$oltNombre.ip\"}] },\n"
		      + "        	            {\n"
		      + "        	                ip:\"$ip_olt\",\n"
		      + "        	                nombre:\"$nombre_olt\",\n"
		      + "        	                estatus: NumberInt(0)\n"
		      + "        	            } \n"
		      + "	          ] }\n"
		      + "	    }\n"
		      + "	\n"
		      + "}\n"
		      , "{ $replaceRoot: { newRoot: \"$oltNombre\" } }"
	  		})
	List<CatOltsEntity> getFaltantes();
	
}
