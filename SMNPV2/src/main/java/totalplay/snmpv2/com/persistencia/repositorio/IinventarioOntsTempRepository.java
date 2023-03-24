package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsTempRepository extends MongoRepository<InventarioOntsTmpEntity, String> {
	
	@Aggregation(pipeline = { 
		    "{$match:{}}"})
	List<InventarioPuertosEntity> getAll();
	
	@Aggregation(pipeline = { 
	  "{$match:{estatus:1}}\n"
    , "{$out:'tb_inventario_onts_aux'}"})
	@Meta(allowDiskUse = true)
	void sendToAux();
	
	
	
	@Aggregation(pipeline = { 
		    "{$unionWith:\"tb_inventario_onts_erroneas\"}\n"
		  , "    {$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
		  , "    {\n"
		  + "        $lookup:{\n"
		  + "            from: \"tb_inventario_onts_puertos\",\n"
		  + "            localField:\"index\",\n"
		  + "            foreignField: \"index\",\n"
		  /*+ "            let: {olt:\"$id_olt\", region:\"$id_region\", oid:\"$oid\" },\n"
		  + "            pipeline:[\n"
		  + "                { $match:\n"
		  + "                    { $expr:\n"
		  + "                        { $and:[ \n"
		  + "                            { $eq: [ \"$oid\", \"$$oid\" ] },\n"
		  + "                            { $eq: [ \"$id_olt\", \"$$olt\" ] },\n"
		  + "                            { $eq: [ \"$id_region\", \"$$region\" ] },\n"
		  + "                        ]}\n"
		  + "                     }\n"
		  + "                }\n"
		  + "            ],\n"
		  */+ "            as:\"puertos\"\n"
		  + "        \n"
		  + "        }    \n"
		  + "    }\n"
		  , "	{\n"
		  + "	    $unwind:{ path:\"$puertos\",  preserveNullAndEmptyArrays: true}\n"
		  + "	}\n"
		  , "    {$match:{puertos:null}}  \n"
		  , "    {$project:{\n"
		  + "        oid : \"$oid\",\n"
		  + "        uid : \"$uid\",\n"
		  + "        puerto : \"$id_puerto\",\n"
		  + "        id_olt : \"$id_olt\",\n"
		  + "        id_region : \"$id_region\",\n"
		  + "        fecha_descubrimiento: \"$fecha_poleo\",\n"
		  + "        index: \"$index\",\n"
		  + "        puertos:\"$puertos\"\n"
		  + "    }}\n"
		  , "    {$unset:[\"_id\", \"puertos\"]}"})
	List<InventarioPuertosEntity> findFaltantesPuertos(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	@Aggregation(pipeline = { 
		        "{\"$group\": {\n"
		      + "        \"_id\": \"$numero_serie\",\n"
		      + "         \"count\": { \"$sum\": 1  },\n"
		      + "  }\n"
		      + "}\n"
		      , "{\n"
		      + "   $match:{count:{$gte:2}}\n"
		      + "}\n"
		      , "{\n"
		      + "   $set:{serie:\"$_id\"}\n"
		      + "}\n"
		      , "{\n"
		      + "	\"$lookup\":{\n"
		      + "		from: \"tb_inventario_onts_tmp\",\n"
		      + "		localField: \"serie\",\n"
		      + "    foreignField: \"numero_serie\",\n"
		      + "		as: \"onts\",\n"
		      + "	}			\n"
		      + "}\n"
		      , "{\n"
		      + "    $unwind:\"$onts\"\n"
		      + "}\n"
		      , "{ $replaceRoot: { newRoot: \"$onts\" } }\n"
		      , "{ $set: {estatus:0}}\n"
		      , "{ $merge: { into: \"tb_inventario_onts_tmp\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
		      })
	@Meta(allowDiskUse = true)
	void findDuplicadas();
	
	
	@Aggregation(pipeline = { 
	          "{$match:{estatus:0}},\n"
	        , "{\n"
	        + "  $lookup:{\n"
	        + "    from: \"auxiliar\",\n"
	        + "    localField:\"index\",\n"
	        + "    foreignField: \"index\",\n"
	        + "    as:\"metrica\"        \n"
	        + "  }    \n"
	        + "}\n"
	        , "	{\n"
  		    + "	    $unwind:{ path:\"$metrica\",  preserveNullAndEmptyArrays: true}\n"
  		    + "	}\n"
	        , "{\n"
	        + "	$set:{estatus:{$ifNull:[\"$metrica.estatus\", 0]}}\n"
	        + "}\n"
	        , "{$unset:[\"metrica\"]}\n"
	        , "{$out: \"tb_diferencias\" }"})
	@Meta(allowDiskUse = true)
	List<InventarioOntsTmpEntity> sendTbDiferencias();

	@Query(value = "{'id_olts': ?0}", count = true)
	Integer finOnts(@Param("idOlts") Integer idOlts);
}
