package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.LimpiezaManualDto;
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

	@Query(value = "{'id_olt': ?0}", count = true)
	Integer finOnts(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = { 
	          	  "{'$group': {\n"
	          	+ "		'_id': '$numero_serie',\n"
	          	+ "		'count': { '$sum': 1  },\n"
	          	+ "		'original':{$push:'$$ROOT'}\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{$unwind:'$original'}\n"
	          	, "{	$set:{'original.count':'$count'}}\n"
	          	, "{ $replaceRoot: { newRoot: '$original'} },\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'tb_diferencias',\n"
	          	+ "		localField:'numero_serie',\n"
	          	+ "		foreignField:'numero_serie',\n"
	          	+ "		let: {olt:'$id_olt', serie:'$numero_serie' },\n"
	          	+ "		pipeline:[\n"
	          	+ "			{ $match:\n"
	          	+ "				{ $expr:\n"
	          	+ "					{ $and:[ \n"
	          	+ "						{ $eq: [ '$numero_serie', '$$serie' ] },\n"
	          	+ "						{ $eq: [ '$id_olt', '$$olt' ] },\n"
	          	+ "					]}\n"
	          	+ "				 }\n"
	          	+ "			}\n"
	          	+ "		],\n"
	          	+ "		as:'duplicados'\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{$match: {duplicados:{$eq:[]}} }\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'auxiliar_descubrimiento_manual',\n"
	          	+ "		localField:'index',\n"
	          	+ "		foreignField:'index',\n"
	          	+ "		as:'estatus'\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{$unwind: {path: '$estatus', preserveNullAndEmptyArrays: true}}\n"
	          	, "{\n"
	          	+ "		\"$lookup\":{\n"
	          	+ "			from: \"tb_inventario_onts\",\n"
	          	+ "			localField:\"index\",\n"
	          	+ "			foreignField:\"index\",\n"
	          	+ "         as:\"onts\"\n"
	          	+ "		}\n"
	          	+ "}\n"
	          	, "{$unwind: {path: '$onts', preserveNullAndEmptyArrays: true}}"
	          	, "{ $set:{ estatus: {$ifNull:[{$cond: [ { $eq: [ \"$estatus.estatus\", 0 ] },null, \"$estatus.estatus\" ]}, \"$onts.estatus\", 0]} } }\n"
	          	, "{ $unset:['duplicados', 'class']}\n"
	          	, "{	\n"
	          	+ "	'$group': {\n"
	          	+ "		'_id': '$numero_serie', \n"
	          	+ "		'datos':{ $push:'$$ROOT' },\n"
	          	+ "		count:{ $push:'$count' }	   \n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{	$set:{count:{$arrayElemAt : ['$count',0] }} }\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'tb_diferencias',\n"
	          	+ "		localField:'_id',\n"
	          	+ "		foreignField:'numero_serie',\n"
	          	+ "		let: {olts:'$datos.id_olt', serie:'$_id' },\n"
	          	+ "		pipeline:[\n"
	          	+ "			{ $match:\n"
	          	+ "				{ $expr:\n"
	          	+ "					{ $and:[ \n"
	          	+ "						{ $eq: [ '$numero_serie', '$$serie' ] },\n"
	          	+ "						{$not:{ $in: [ '$id_olt', '$$olts' ] }},\n"
	          	+ "					]}\n"
	          	+ "				 }\n"
	          	+ "			}\n"
	          	+ "		],\n"
	          	+ "		as:'duplicadosdup'\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'tb_diferencias_carga_manual',\n"
	          	+ "		localField:'_id',\n"
	          	+ "		foreignField:'numero_serie',\n"
	          	+ "		let: {olts:'$datos.id_olt', serie:'$_id' },\n"
	          	+ "		pipeline:[\n"
	          	+ "			{ $match:\n"
	          	+ "				{ $expr:\n"
	          	+ "					{ $and:[ \n"
	          	+ "						{ $eq: [ '$numero_serie', '$$serie' ] },\n"
	          	+ "						{ $not:{ $in: [ '$id_olt', '$$olts' ] }},\n"
	          	+ "					]}\n"
	          	+ "				 }\n"
	          	+ "			}\n"
	          	+ "		],\n"
	          	+ "		as:'manual'\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'tb_inventario_onts',\n"
	          	+ "		localField:'_id',\n"
	          	+ "		foreignField:'numero_serie',\n"
	          	+ "		let: {olts:'$datos.id_olt', serie:'$_id' },\n"
	          	+ "		pipeline:[\n"
	          	+ "			{ $match:\n"
	          	+ "				{ $expr:\n"
	          	+ "					{ $and:[ \n"
	          	+ "						{ $eq: [ '$numero_serie', '$$serie' ] },\n"
	          	+ "						{$not:{ $in: [ '$id_olt', '$$olts' ] } },\n"
	          	+ "					]}\n"
	          	+ "				 }\n"
	          	+ "			}\n"
	          	+ "		],\n"
	          	+ "		as:'inventariodup'\n"
	          	+ "	}\n"
	          	+ "}\n"
	          	, "{\n"
	          	+ "	'$lookup':{\n"
	          	+ "		from: 'tb_inventario_onts',\n"
	          	+ "		localField:'_id',\n"
	          	+ "		foreignField:'numero_serie',\n"
	          	+ "		let: {olts:'$datos.id_olt', serie:'$_id' },\n"
	          	+ "		pipeline:[\n"
	          	+ "			{ $match:\n"
	          	+ "				{ $expr:\n"
	          	+ "					{ $and:[ \n"
	          	+ "						{ $eq: [ '$numero_serie', '$$serie' ] },\n"
	          	+ "						{ $in: [ '$id_olt', '$$olts' ] } ,\n"
	          	+ "					]}\n"
	          	+ "				 }\n"
	          	+ "			}\n"
	          	+ "		],\n"
	          	+ "		as:'inventario'\n"
	          	+ "	}\n"
	          	+ "}   \n"
	          	, "{\n"
      			+ "	$set:{\n"
      			+ "		categoria: { $cond: [ {$ne :[ '$duplicadosdup', [] ]},  \n"
      			+ "			   { $cond: [  {$ne :[ '$inventariodup', [] ]}, \n"
      			+ "				   { $cond: [  {$ne :[ '$manual', [] ]}, 6, 3 ] }, \n"
      			+ "					{ $cond: [  {$ne :[ '$manual', [] ]}, 7, 2 ] },  ] }, \n"
      			+ "			   { $cond: [  {$gt :[ '$count', 1  ]}, \n"
      			+ "						 { $cond: [  {$ne :[ '$inventario', []  ]}, \n"
      			+ "						  5, \n"
      			+ "						  4 ] },\n"
      			+ "					 1 ] }\n"
      			+ "		   ] }\n"
      			+ "	}\n"
      			+ "} \n"
      			, "{$project:{\n"
				+ "           categoria:\"$categoria\",\n"
				+ "           onts: { $cond: [ {$eq :[ \"$categoria\", 1  ]},            \n"
				+ "                    {\n"
				+ "                      inventarioAux: \"$datos\"\n"
				+ "                    }, \n"
				+ "                    { $cond: [ {$eq :[ \"$categoria\", 2  ]}, \n"
				+ "                       {\n"
				+ "                           duplicadas:\"$datos\",\n"
				+ "                           manual:{ $concatArrays: [ \"$datos\", \"$duplicadosdup\" ] }\n"
				+ "                       }, \n"
				+ "                       { $cond: [ {$eq :[ \"$categoria\",  3  ]}, \n"
				+ "                            {\n"
				+ "                                eliminar:\"$inventariodup\",\n"
				+ "                                duplicadas: \"$datos\",\n"
				+ "                                manual:{ $concatArrays: [ \"$datos\", \"$duplicadosdup\" ] }\n"
				+ "                            }, \n"
				+ "                            { $cond: [{$eq :[ \"$categoria\",  4  ]}, \n"
				+ "                                {\n"
				+ "                                    duplicadas:\"$datos\",\n"
				+ "                                    manual:\"$datos\"\n"
				+ "                                }, \n"
				+ "                                { $cond: [ {$eq :[ \"$categoria\",  5  ]}, \n"
				+ "                                    {\n"
				+ "                                        eliminar:\"$inventario\",\n"
				+ "                                        duplicadas: \"$datos\",\n"
				+ "                                        manual:\"$datos\"\n"
				+ "                                    }, \n"
				+ "                                    { $cond: [ {$eq :[ \"$categoria\",  6  ]}, \n"
				+ "                                        {\n"
				+ "                                             eliminar:\"$inventariodup\",\n"
				+ "                                             duplicadas: \"$datos\",\n"
				+ "                                              manual:\"$datos\"\n"
				+ "                                        }, \n"
				+ "                                        { $cond: [ {$eq :[ \"$categoria\",  7  ]}, \n"
				+ "                                            {\n"
				+ "                                                duplicadas:\"$datos\",\n"
				+ "                                                manual:\"$datos\"\n"
				+ "                                            }, \n"
				+ "                                            {} ] }\n"
				+ "                                         ] }\n"
				+ "                                     ] }\n"
				+ "                                 ] }\n"
				+ "                            ] }\n"
				+ "                    ] }\n"
				+ "                     \n"
				+ "                ] }\n"
				+ "           }\n"
				+ "}"
	          	, "{\n"
	          	+ "	'$group': {\n"
	          	+ "		'_id': null,\n"
	          	+ "		inventarioAux:{$push: '$onts.inventarioAux'},\n"
	          	+ "		duplicadas:{$push: '$onts.duplicadas'},\n"
	          	+ "		manual:{$push: '$onts.manual'},\n"
	          	+ "		eliminar:{$push: '$onts.eliminar'},\n"
	          	+ "	},\n"
	          	+ "} \n"
	          	, "{\n"
	          	+ "	$project: {\n"
	          	+ "		inventarioAux: {\n"
	          	+ "			$reduce: {\n"
	          	+ "			  input: '$inventarioAux',\n"
	          	+ "			  initialValue: [],\n"
	          	+ "			  in: { '$concatArrays': ['$$this', '$$value'] }\n"
	          	+ "			}\n"
	          	+ "		},\n"
	          	+ "		duplicados: {\n"
	          	+ "			$reduce: {\n"
	          	+ "			  input: '$duplicadas',\n"
	          	+ "			  initialValue: [],\n"
	          	+ "			  in: { '$concatArrays': ['$$this', '$$value'] }\n"
	          	+ "			}\n"
	          	+ "		},\n"
	          	+ "		manual: {\n"
	          	+ "			$reduce: {\n"
	          	+ "			  input: '$manual',\n"
	          	+ "			  initialValue: [],\n"
	          	+ "			  in: { '$concatArrays': ['$$this', '$$value'] }\n"
	          	+ "			}\n"
	          	+ "		},\n"
	          	+ "		eliminar: {\n"
	          	+ "			$reduce: {\n"
	          	+ "			  input: '$eliminar',\n"
	          	+ "			  initialValue: [],\n"
	          	+ "			  in: { '$concatArrays': ['$$this', '$$value'] }\n"
	          	+ "			}\n"
	          	+ "		}       \n"
	          	+ "	}       \n"
	          	+ "},\n"
	          	, "{$unset:['inventarioAux._id', 'manual._id', 'duplicados._id']}"
	          })
	@Meta(allowDiskUse = true)
	List<LimpiezaManualDto> getOntsInventarios();
}
