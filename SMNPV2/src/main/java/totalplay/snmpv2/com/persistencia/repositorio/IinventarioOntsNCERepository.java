package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsNCERepository extends MongoRepository<InventarioNCEEntity, String> {
	
	@Aggregation(pipeline = {
			 // "{$match:{sn:\"485754431820539B\"}}"	
			  "{\n"
			+ "		\"$lookup\":{\n"
			+ "			from: \"tb_inventario_onts\",\n"
			+ "			localField: \"sn\",\n"
			+ "            foreignField: \"numero_serie\",\n"
			+ "			as: \"onts\",\n"
			+ "		}			\n"
			+ "	}\n"
			, "	{ $match:{onts:{$eq:[]}} }\n"
			, "	{\n"
			+ "		\"$lookup\":{\n"
			+ "			from: \"cat_olts\",\n"
			+ "			localField: \"ip_olt\",\n"
			+ "            foreignField: \"ip\",\n"
			+ "			as: \"olt\",\n"
			+ "		}			\n"
			+ "	}\n"
			, "	{$unwind: \"$olt\"}\n"
			, "	{\n"
			+ "	    $project:{\n"
			+ "	          \"alias\": \"$etiqueta_ont\",\n"
			+ "              \"id_olt\": \"$olt.id_olt\",\n"
			+ "              \"fecha_poleo\":{$dateSubtract:{ startDate: new Date(), unit: \"hour\", amount: 6}},\n"
			+ "              \"id_ejecucion\": ?0,\n"
			+ "              \"id_region\": \"$olt.id_region\",\n"
			+ "              \"frame\": \"$frame\",\n"
			+ "              \"slot\": \"$slot\",\n"
			+ "              \"port\": \"$port\",\n"
			+ "              \"numero_serie\": \"$sn\",\n"
			+ "              \"tecnologia\": \"$olt.tecnologia\",\n"
			+ "	    \n"
			+ "	    } \n"
			+ "	}"
			})
	List<InventarioOntsEntity> getFaltantesInv(@Param("idEjecucion") String idEjecucion );
	
	
	@Aggregation(pipeline = {
			    "{\n"
			  + "		\"$lookup\":{\n"
			  + "			from: \"tb_inventario_onts\",\n"
			  + "			localField: \"sn\",\n"
			  + "            foreignField: \"numero_serie\",\n"
			  + "			as: \"onts\",\n"
			  + "		}			\n"
			  + "}\n"
			  , "{$match:{onts:{$ne:[]}} }\n"
			  , "{\n"
			  + "		\"$lookup\":{\n"
			  + "			from: \"cat_olts\",\n"
			  + "			localField: \"ip_olt\",\n"
			  + "            foreignField: \"ip\",\n"
			  + "			as: \"olt\",\n"
			  + "		}			\n"
			  + "}\n"
			  , "{$unwind: \"$olt\"}\n"
			  , "{$unwind: \"$onts\"}\n"
			  , "{$match:{$expr:{$ne:[\"$olt.id_olt\", \"$onts.id_olt\"]}} }"
			})
	InventarioOntsEntity getDistinctOlt(@Param("idEjecucion") String idEjecucion );
	
}




