package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsAuxRepository extends MongoRepository<InventarioOntsAuxEntity, String> {

	@Aggregation(pipeline = { 
		      "{$out:'tb_inventario_onts'}"
	  		})
	void sendToInventario();
	
	
	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
	  		+ "        $lookup:{\n"
	  		+ "            from: \"auxiliar\",\n"
	  		+ "            localField:\"index\",\n"
	  		+ "            foreignField: \"index\",          \n"
	  		+ "            as:\"metrica\"\n"
	  		+ "        \n"
	  		+ "        }    \n"
	  		+ " }\n"
	  		, "{\n"
	  		+ "        $lookup:{\n"
	  		+ "            from: \"tb_inventario_onts\",\n"
	  		+ "            localField:\"index\",\n"
	  		+ "            foreignField: \"index\",          \n"
	  		+ "            as:\"inventario\"\n"
	  		+ "        \n"
	  		+ "        }    \n"
	  		+ " }\n"
	  		, " {\n"
	  		+ "        $set:{\n"
	  		+ "            estatus:{$ifNull:[{$cond: [ { $eq: [ {$arrayElemAt : ['$metrica.estatus',0]}, 0 ] },null, {$arrayElemAt : ['$metrica.estatus',0]}  ]}, {$arrayElemAt : ['$inventario.estatus',0]}, 0 ]}\n"
	  		+ "        }\n"
	  		+ "}\n"
	  		, "{$unset:[\"metrica\", '_id', 'inventario']}\n"
	  		//, "{ $merge: { into: \"tb_inventario_onts_aux\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
	  		})
	List<InventarioAuxTransEntity> getEstatus(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	
	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar\",\n"
			+ "            localField:\"index\",\n"
			+ "            foreignField: \"index\",          \n"
			+ "            as:\"metrica\"\n"
			+ "        \n"
			+ "        }    \n"
			+ "}\n"
			, " {\n"
			+ "        $set:{\n"
			+ "            descripcionAlarma:{$ifNull:[{$arrayElemAt : ['$metrica.valor',0]}, '']}\n"
			+ "        }\n"
			+ "}\n"
			, "{$unset:[\"metrica\", '_id']}\n"
			//, " { $merge: { into: \"tb_inventario_onts_aux\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
			})
	List<InventarioAuxTransEntity> getDescripcionAlarma(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar\",\n"
			+ "            localField:\"index\",\n"
			+ "            foreignField: \"index\",          \n"
			+ "            as:\"metrica\"\n"
			+ "        \n"
			+ "        }    \n"
			+ " }\n"
			, " {\n"
			+ "        $set:{\n"
			+ "            lastDownTime:{$ifNull:[{$arrayElemAt : ['$metrica.valor',0]}, '']}\n"
			+ "        }\n"
			+ "}\n"
			, "{$unset:[\"metrica\", '_id']}\n"
			//, " { $merge: { into: \"tb_inventario_onts_aux\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
			})
	List<InventarioAuxTransEntity> getLastDownTime(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);

	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar\",\n"
			+ "            localField:\"index\",\n"
			+ "            foreignField: \"index\",          \n"
			+ "            as:\"metrica\"\n"
			+ "        \n"
			+ "        }    \n"
			+ " }\n"
			, " {\n"
			+ "        $set:{\n"
			+ "            alias:{$ifNull:[{$arrayElemAt : ['$metrica.valor',0]}, '']}\n"
			+ "        }\n"
			+ "}\n"
			, "{$unset:[\"metrica\", '_id']}\n"
			//, " { $merge: { into: \"tb_inventario_onts_aux\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
			})
	List<InventarioAuxTransEntity> getAlias(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);

	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar\",\n"
			+ "            localField:\"indexFSP\",\n"
			+ "            foreignField: \"index\",          \n"
			+ "            as:\"metrica\"\n"
			+ "        \n"
			+ "        }    \n"
			+ " }\n"
			, " {\n"
			+ "        $set:{\n"
			+ "            frame:{$ifNull:[{$arrayElemAt : ['$metrica.frame',0]}, '']},\n"
			+ "            slot:{$ifNull:[{$arrayElemAt : ['$metrica.slot',0]}, '']},\n"
			+ "            port:{$ifNull:[{$arrayElemAt : ['$metrica.port',0]}, '']}\n"
			+ "        }\n"
			+ " }\n"
			, "{$unset:[\"metrica\", '_id']}\n"
			//, "{ $merge: { into: \"tb_inventario_onts_aux\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
			})
	List<InventarioAuxTransEntity> getFrameSlotPort(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);

	@Aggregation(pipeline = { 
		        "{\n"
		      + "        \"$group\": {\n"
		      + "                \"_id\": \"$index\", \n"
		      + "                \"count\": { \"$sum\": 1 },   \n"
		      + "                \"datos\":{\n"
		      + "                    $push:{\n"
		      + "                        ont:\"$_id\",\n"
		      + "                        fecha_poleo:\"$fecha_poleo\" \n"
		      + "                    }\n"
		      + "                }  \n"
		      + "          },\n"
		      + "} \n"
		      , "{ $match:{count:{$gt:1}} }\n"
		      , "{\n"
		      + "        $set: {\n"
		      + "            datos: {\n"
		      + "                $let: {\n"
		      + "                    vars: {\n"
		      + "                        maxDate: { $max: \"$datos.fecha_poleo\" }\n"
		      + "                    },\n"
		      + "                    in: { $filter: { input: \"$datos\", cond: { $lt: [  \"$$this.fecha_poleo\",\"$$maxDate\", ] } } },                   \n"
		      + "                }\n"
		      + "            }\n"
		      + "        }\n"
		      + " }\n"
		      , " {\"$unwind\": \"$datos\"}\n"
		      , " {\n"
		      + "		\"$lookup\":{\n"
		      + "			from: \"tb_inventario_onts_aux\",\n"
		      + "			localField:\"datos.ont\",\n"
		      + "			foreignField:\"_id\",\n"
		      + "			as: \"onts\",\n"
		      + "			\n"
		      + "		}\n"
		      + " }\n"
		      , " {\"$unwind\": \"$onts\"}\n"
		      , " { $replaceRoot: { newRoot: \"$onts\" } }\n"
		      , " {\n"
		      + "        $set:{\n"
		      + "            oid:null,\n"
		      + "            puerto:null,\n"
		      + "            uid:null,\n"
		      + "            index:null,\n"
		      + "            indexFSP:null,\n"
		      + "            descripcion:\"$oid\"\n"
		      + "            \n"
		      + "            \n"
		      + "            \n"
		      + "        }\n"
		      + "    \n"
		      + " }"
			})
	@Meta(allowDiskUse = true)
	List<InventarioOntsAuxEntity> getOidsRepetidos();


}


