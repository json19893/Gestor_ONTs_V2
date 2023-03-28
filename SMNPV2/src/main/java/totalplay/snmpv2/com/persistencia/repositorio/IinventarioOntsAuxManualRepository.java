package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsAuxManualRepository extends MongoRepository<InventarioOntsAuxManualEntity, String> {

	@Aggregation(pipeline = { 
		      "{$match:{$and:[{id_region:?0},{id_olt:?1}]}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar_descubrimiento_manual\",\n"
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
			+ "            from: \"auxiliar_descubrimiento_manual\",\n"
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
			+ "            from: \"auxiliar_descubrimiento_manual\",\n"
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
			+ "            from: \"auxiliar_descubrimiento_manual\",\n"
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
		      + "	'$lookup':{\n"
		      + "		from: 'tb_inventario_onts',\n"
		      + "		localField:'numero_serie',\n"
		      + "		foreignField:'numero_serie',\n"
		      + "		as: 'inventario',\n"
		      + "	}\n"
		      + "}\n"
		      , "{\n"
		      + "    $unwind:{ path:'$inventario',  preserveNullAndEmptyArrays: true}\n"
		      + "}\n"
		      , "{\n"
		      + "	$set:{\n"
		      + "	    _id:'$inventario._id',\n"
		      + "	    fecha_modificacion:{$dateSubtract:{ startDate: new Date(), unit: 'hour', amount: 6}},\n"
		      + "	    fecha_poleo:{$ifNull:['$inventario.fecha_poleo', '$fecha_poleo']}\n"
		      + "	}\n"
		      + "}\n"
		      , "{$unset:['inventario']}\n"
		      , "{ $merge: { into: 'tb_inventario_onts', on: '_id', whenMatched: 'replace', whenNotMatched: 'insert' } }\n"
		      + ""
			})
	@Meta(allowDiskUse = true)
	List sentToInv();
	
	
	@Aggregation(pipeline = { 
	        "{\n"
	      + "	'$lookup':{\n"
	      + "		from: 'tb_inventario_onts',\n"
	      + "		localField:'numero_serie',\n"
	      + "		foreignField:'numero_serie',\n"
	      + "		as: 'inventario',\n"
	      + "	}\n"
	      + "}\n"
	      , "{\n"
	      + "    $unwind:{ path:'$inventario',  preserveNullAndEmptyArrays: true}\n"
	      + "}\n"
	      , "{\n"
	      + "	$set:{\n"
	      + "	    _id:'$inventario._id',\n"
	      + "	    fecha_modificacion:{$dateSubtract:{ startDate: new Date(), unit: 'hour', amount: 6}},\n"
	      + "	    fecha_poleo:{$ifNull:['$inventario.fecha_poleo', '$fecha_poleo']}\n"
	      + "	}\n"
	      + "}\n"
	      , "{$unset:['inventario']}\n"
	      //, "{ $merge: { into: 'tb_inventario_onts', on: '_id', whenMatched: 'replace', whenNotMatched: 'insert' } }\n"
	      
		})
	@Meta(allowDiskUse = true)
	List<InventarioOntsEntity> getInv();
	
}


