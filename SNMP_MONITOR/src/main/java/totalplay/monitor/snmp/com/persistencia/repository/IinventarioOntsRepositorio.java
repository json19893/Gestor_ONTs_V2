package totalplay.monitor.snmp.com.persistencia.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.responseOltsOntsDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOntsEmpDto;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;


@Repository
public interface IinventarioOntsRepositorio extends MongoRepository<inventarioOntsEntidad, String> {
	//inventarioOntsEntidad findByOidAndIdOlts(String oid, Integer id_olt);
	@Aggregation(pipeline = { "{'$match':{'$and':[{'oid':?0},{'id_olt':?1}]}}" })
	inventarioOntsEntidad finOntsByOidAndIdOlts(@Param("oid") String oid, @Param("id_olt") Integer id_olt);
	
	@Aggregation(pipeline = { "{'$match':{'numero_serie':?0}}" })
	inventarioOntsEntidad getOntBySerie(@Param("serie") String serie);
	
	
	/*@Aggregation(pipeline = { "{'$limit':1}" })
	inventarioOntsEntidad finOntsByFecha();*/
	//@Aggregation(pipeline = { "{'$match':{'$and':[{'id_region':?0},{'estatus':?1}]},{$count:'total' }" })
	@Query(value="{'id_region': ?0, 'estatus': ?1}",count=true)
	Integer finOntsByEstatusRegion(@Param("idRegion") Integer idRegion, @Param("estatus") Integer estatus);

	@Query(value = " {'id_region': ?0 }",count = true)
	Integer finOntsByTotalRegion(@Param("idRegion") Integer idRegion);
	
	@Query(value = " {'id_olt': ?0 }",count = true)
	Integer finOntsByTotalOlt(@Param("idOlt") Integer idOlt);
	
	@Query(value = " {'id_olt': ?0, 'estatus': ?1 }",count = true)
	Integer finOntsByTotalEstatus(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus);
	
	@Query(value = " {}",count = true)
	Integer finOntsByTotal();
	
	@Query(value = " {'vip':1}",count = true)
	Integer finOntsByClasificionV();
	
	@Query(value = "{'tipo':'E'}",count = true)
	Integer  findCatOltsByTipo();
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_region':?0},{'tecnologia':?1}, {'estatus':?2}, {'tipo':?3}]}}" })
	List<inventarioOntsEntidad> totalByRegionTecnologiaEstatus(@Param("id_region") Integer id_region,@Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus, @Param("tipo") String tipo);
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'tecnologia':?0}, {'estatus':?1}, {'tipo':?2}]}}" })
	List<inventarioOntsEntidad> totalByTecnologiaEstatus(@Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus, @Param("tipo") String tipo);
	
	@Query(value = " {'id_region': ?0, 'tecnologia': ?1, 'estatus': ?2, 'tipo': ?3 }",count = true)
	Integer totalByRegionTecnologiaEstatusCount(@Param("id_region") Integer id_region,@Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus, @Param("tipo") String tipo);

	@Query(value = " { 'tecnologia': ?0, 'estatus': ?1, 'tipo': ?2}",count = true)
	Integer totalByTecnologiaEstatusCount(@Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus, @Param("tipo") String tipo);

	@Query(value = " {'id_olt': ?0, 'tipo': 'E' }",count = true)
	Integer finOntsByTotalOltEmp(@Param("idOlt") Integer idOlt);
	
	@Query(value = " {'id_olt': ?0, 'estatus': ?1, 'tipo': 'E' }",count = true)
	Integer finOntsByTotalEstatusEmp(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus);
	
	@Query(value = " {'id_olt': ?0, 'vip': 1 }",count = true)
	Integer finOntsByTotalOltVip(@Param("idOlt") Integer idOlt);
	
	@Query(value = " {'id_olt': ?0, 'estatus': ?1, 'vip': 1 }",count = true)
	Integer finOntsByTotalEstatusVip(@Param("idOlt") Integer idOlt,@Param("estatus") Integer estatus);
	
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}"
			,"{'$match':{'numero_serie':?0} }"})
	List<inventarioOntsEntidad> findOntBySerieT(@Param("serie") String serie);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'alias':?0} } "})
	List<inventarioOntsEntidad> findOntByAliasT(@Param("alias") String alias);
	
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}"
			, "{'$match':{'$and':[{'numero_serie':?0},{'tipo':'E'}]}}" })
	List<inventarioOntsEntidad> findOntBySerieE(@Param("serie") String serie);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'$and':[{'alias':?0},{'tipo':'E'}]}}" })
	List<inventarioOntsEntidad> findOntByAliasE(@Param("alias") String alias);
	
	@Query("{numero_serie: ?0}")
	inventarioOntsEntidad getOntBySerialNumber(String numSerie);
	
	
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}"
			, "{'$match':{'$and':[{'numero_serie':?0},{'vip': 1 }]}}" })
	List<inventarioOntsEntidad> findOntBySerieV(@Param("serie") String serie);
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'alias':?0},{'vip':1}]}}" })
	List<inventarioOntsEntidad> findOntByAliasV(@Param("alias") String alias);
	
	@Query(value="{'alias': {$regex: ?0, $options:'i'}}" )
	List<inventarioOntsEntidad> findAliasByRegex(@Param("regex") String regex);

	//@Query(value="{'numero_serie': {$regex: ?0, $options:'i'}}" )
	@Aggregation(pipeline = { "{ \n"
			+ "    '$unionWith': {\n"
			+ "      'coll': 'tb_inventario_onts_pdm'\n"
			+ "    }\n"
			+ "  }"
			, "{\n"
			+ "    '$match': {\n"
			+ "      'numero_serie': new RegExp('.*"+"?0"+".*')\n"
			+ "    }\n"
			+ "  }" })
	List<inventarioOntsEntidad> findSerieByRegex(@Param("regex") String regex);
	
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}"
			," {$match: {$and: [{tipo: 'E',},{id_region: ?0,}]}}"
			,"{\n"
			+ "    $group: {\n"
			+ "      _id: {\n"
			+ "        tecnologia: '$tecnologia',\n"
			+ "        estatus: '$estatus',\n"
			+ "      },\n"
			+ "      count: {$sum: 1,},\n"
			+ "    },\n"
			+ "  }\n"
			, "  {\n"
			+ "    $group: {\n"
			+ "      _id: '$_id.tecnologia',\n"
			+ "      total: {$sum: '$count',},\n"
			+ "      datos: {\n"
			+ "        $push: {\n"
			+ "          estatus: '$_id.estatus',\n"
			+ "          count: '$count',\n"
			+ "        },\n"
			+ "      },\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $set: {\n"
			+ "      arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 1],},},},0],},\n"
			+ "      abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 2],},},},0,],},\n"
			+ "      sin_informacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 0],},},},0,],},\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $set: {\n"
			+ "		 tecnologia:{$ifNull: ['$_id',''],},\n"
			+ "      arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count', 0],},NumberInt(0),],},\n"
			+ "      abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count', 0],},NumberInt(0),],},\n"
			+ "      sin_informacion: {$ifNull: [{$arrayElemAt: ['$sin_informacion.count', 0],},NumberInt(0),],},\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $unset: ['datos', '_id'],\n"
			+ "  }"})
	List<totalesOntsEmpDto> getConteoByEmp(@Param("region") Integer region);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}"
			," {$match: {tipo: 'E'}}"
			,"{\n"
			+ "    $group: {\n"
			+ "      _id: {\n"
			+ "        tecnologia: '$tecnologia',\n"
			+ "        estatus: '$estatus',\n"
			+ "      },\n"
			+ "      count: {$sum: 1,},\n"
			+ "    },\n"
			+ "  }\n"
			, "  {\n"
			+ "    $group: {\n"
			+ "      _id: '$_id.tecnologia',\n"
			+ "      total: {$sum: '$count',},\n"
			+ "      datos: {\n"
			+ "        $push: {\n"
			+ "          estatus: '$_id.estatus',\n"
			+ "          count: '$count',\n"
			+ "        },\n"
			+ "      },\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $set: {\n"
			+ "      arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 1],},},},0],},\n"
			+ "      abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 2],},},},0,],},\n"
			+ "      sin_informacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus', 0],},},},0,],},\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $set: {\n"
			+ "		 tecnologia: {$ifNull: ['$_id',''],},\n	"
			+ "      arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count', 0],},NumberInt(0),],},\n"
			+ "      abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count', 0],},NumberInt(0),],},\n"
			+ "      sin_informacion: {$ifNull: [{$arrayElemAt: ['$sin_informacion.count', 0],},NumberInt(0),],},\n"
			+ "    },\n"
			+ "  }"
			,"{\n"
			+ "    $unset: ['datos', '_id'],\n"
			+ "  }"})
	List<totalesOntsEmpDto> getAllOntEmp();
		
	@Aggregation(pipeline = { 
			"{$match: {tipo: 'E'}}"
			,"{$group: {_id: {id_region: '$id_region',estatus: '$estatus'},\n"
					+ "		count: {$sum: 1}}}"
			,"{ $group: {_id: '$_id.id_region',\n"
			+ "	count: {$sum: '$count'},\n"
			+ "	datos: {$push: {estatus: '$_id.estatus',count: '$count'}}\n"
			+ "}}"
			,"{$unionWith: {\n"
					+ "  coll: 'cat_olts',\n"
					+ "  pipeline: [\n"
					+ "   {$group: { _id: '$id_region'}}\n"
					+ "  ]}\n"
					+ "}"
			,"{$group: {\n"
					+ "	_id: '$_id',\n"
					+ "	count: {$sum: '$count'},\n"
					+ "	datos: {$push: '$datos'}\n"
					+ "}}"
			,"{$unwind: {\n"
					+ "	path: '$datos',\n"
					+ "	preserveNullAndEmptyArrays: true\n"
					+ "}}"
			,"{$lookup: {\n"
					+ "  from: 'tb_inventario_onts',\n"
					+ "  'let': {region: '$_id'},\n"
					+ "  pipeline: [\n"
					+ "	{$match: {tipo: 'E'}},\n"
					+ "	{$group: {_id: {id_olt: '$id_olt',region: '$id_region'}}},\n"
					+ "    {$lookup: {\n"
					+ "     from: 'cat_olts',\n"
					+ "     localField: '_id.id_olt',\n"
					+ "     foreignField: 'id_olt',\n"
					+ "     as: 'olt'\n"
					+ "	}},\n"
					+ "    {$set: {\n"
					+ "		id_olt: '$_id.id_olt',\n"
					+ "		region: '$_id.region',\n"
					+ "		estatus: {$ifNull: [{$arrayElemAt: ['$olt.estatus',0]},0]}\n"
					+ "    }},\n"
					+ "	{$unset: ['_id','olt']},\n"
					+ "    {$group: {_id: {id_region: '$region',estatus: '$estatus'},\n"
					+ "		count: {$sum: 1}\n"
					+ "    }},\n"
					+ "    {$group: {_id: '$_id.id_region',count: {$sum: '$count'},\n"
					+ "		datos: {$push: {\n"
					+ "			estatus: '$_id.estatus',\n"
					+ "			count: '$count'\n"
					+ "      }}\n"
					+ "    }},\n"
					+ "    {$set: {\n"
					+ "		activas: {$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},\n"
					+ "		inactivas: {$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}}\n"
					+ "	}},\n"
					+ "    {$set: {\n"
					+ "     activas: {$ifNull: [{$arrayElemAt: ['$activas.count',0]},0]},\n"
					+ "     inactivas: {$ifNull: [{$arrayElemAt: ['$inactivas.count',0]},0]}\n"
					+ "    }},\n"
					+ "   {$match: {$expr: {$eq: ['$_id','$$region']}}}\n"
					+ "  ],\n"
					+ "  as: 'olts'\n"
					+ "}}"
					,"{$lookup: {\n"
					+ "  from: 'tb_historico_diferencias',\n"
					+ "  'let': {region: '$_id'},\n"
					+ "  pipeline: [\n"
					+ "   {$match: {tipo: 'E'}},\n"
					+ "   {$group: {\n"
					+ "		_id: '$id_region',\n"
					+ "		count: {$sum: 1}\n"
					+ "    }},\n"
					+ "   {$match: {$expr: {$eq: ['$_id','$$region']}}}\n"
					+ "  ],\n"
					+ "  as: 'cambios'\n"
					+ "}}"
					,"{$set: {\n"
					+ "    arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
					+ "	abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
					+ "	sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]},\n"
					+ "	totalActivas: {$ifNull: [{$arrayElemAt: ['$olts.activas',0]},0]},\n"
					+ "	totalInactivas: {$ifNull: [{$arrayElemAt: ['$olts.inactivas',0]},0]}\n"
					+ "}}"
					,"{$project: {\n"
					+ "	idRegion: '$_id',\n"
					+ "	region: {$concat: ['Región ',{$substr: ['$_id',0,-1]}]},\n"
					+ "	totalOlt: '$totalActivas',\n"
					+ "	totalRegion: {$add: ['$totalActivas','$totalInactivas']},\n"
					+ "	totalOnt: '$count',\n"
					+ "	arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
					+ "	abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
					+ "	sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]},\n"
					+ "	cambios: {$ifNull: [{$arrayElemAt: ['$cambios.count',0]},0]}}\n"
					+ "}"
					,"{$unset: ['_id']}"
					,"{$sort: {idRegion: 1}}"
	})
	List<datosRegionDto> getTotalesEmpresariales();
	
	@Aggregation(pipeline = { 
			"{$unionWith: 'tb_inventario_onts_pdm'}"
			," { '$group': {\n"
			+ "        '_id': {\n"
			+ "            'id_region': '$id_region',\n"
			+ "            'estatus': '$estatus',\n"
			+ "        },\n"
			+ "        'count': { '$sum': 1 },\n"
			+ "        \n"
			+ "    }}"
			," {'$group': {\n"
			+ "                '_id': '$_id.id_region',\n"
			+ "                 'count': { '$sum': '$count'  },\n"
			+ "                 'datos': { \n"
			+ "                    $push:{\n"
			+ "                        estatus:'$_id.estatus',\n"
			+ "                        count:'$count'\n"
			+ "                     \n"
			+ "                    }}                          \n"
			+ "          }\n"
			+ "    }"
			," {\n"
			+ "		'$lookup':{\n"
			+ "			from: 'tb_inventario_onts',\n"
			+ "			let: { region: '$_id'},\n"
			+ "			\n"
			+ "			pipeline: [\n"
			+ "                 { '$group': {\n"
			+ "                    '_id': {\n"
			+ "                        'id_region': '$id_region',\n"
			+ "                        'olts':'$id_olt'\n"
			+ "                    },        \n"
			+ "                }},\n"
			+ "                \n"
			+ "                {'$group': {\n"
			+ "                            '_id': '$_id.id_region',\n"
			+ "                             'count': { '$sum': 1  },                                                     \n"
			+ "                      }\n"
			+ "                },   			    \n"
			+ "			    { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'olts',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "		'$lookup':{\n"
			+ "			from: 'cat_olts',\n"
			+ "			let: { region: '$_id'},			\n"
			+ "			pipeline: [\n"
			+ "	            {\n"
			+ "                    '$group': {\n"
			+ "                            '_id': '$id_region', \n"
			+ "                            'count': { '$sum': 1 },      \n"
			+ "                      },\n"
			+ "               },                \n"
			+ "                			    \n"
			+ "			   { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'total',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "		'$lookup':{\n"
			+ "			from: 'tb_historico_diferencias',\n"
			+ "			let: { region: '$_id'},			\n"
			+ "			pipeline: [\n"
			+ "              {\n"
			+ "                    '$group': {\n"
			+ "                            '_id': '$id_region', \n"
			+ "                            'count': { '$sum': 1 },      \n"
			+ "                      },\n"
			+ "               },            \n"
			+ "                			    \n"
			+ "			   { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'cambios',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "	    $set:{\n"
			+ "            arriba : { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',1]}}} , 0 ]},\n"
			+ "            abajo :  { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',2]}}} , 0 ]},\n"
			+ "            sinInformacion: { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus', 0]}}} , 0 ]},\n"
			+ "            \n"
			+ "        }\n"
			+ "	}"
			,"{\n"
			+ "	    $project:\n"
			+ "           {\n"
			+ "             idRegion:'$_id',\n"
			+ "             region: { $concat: [ 'Región ', {$substr:['$_id', 0, -1 ]} ] },\n"
			+ "             totalOlt: { $ifNull: [  {$arrayElemAt : ['$olts.count',0]}, NumberInt(0)] },\n"
			+ "             totalRegion: { $ifNull: [  {$arrayElemAt : ['$total.count',0]}, NumberInt(0)] },\n"
			+ "             totalOnt: '$count',\n"
			+ "             arriba: { $ifNull: [  {$arrayElemAt : ['$arriba.count',0]}, NumberInt(0)] },\n"
			+ "             abajo: { $ifNull: [  {$arrayElemAt : ['$abajo.count',0]}, NumberInt(0)] }, \n"
			+ "             sinInformacion: { $ifNull: [  {$arrayElemAt : ['$sinInformacion.count',0]}, NumberInt(0)] }, \n"
			+ "             cambios: { $ifNull: [  {$arrayElemAt : ['$cambios.count',0]}, NumberInt(0)] }              \n"
			+ "          }\n"
			+ "          \n"
			+ "	}"
			,"{\n"
			+ "	    $set:{\n"
			+ "	        abajo:{$add: ['$sinInformacion', '$abajo']}\n"
			+ "	    }\n"
			+ "	        \n"
			+ "	}"
			,"{$unset: ['_id']}"
			,"{ $sort : { idRegion : 1 } }"
	})
	List<datosRegionDto> getTotalesRegion();
	
	@Aggregation(pipeline = { 
			"{$match: {vip: 1}}"
			,"{$group: {_id: {id_region: '$id_region',estatus: '$estatus'},\n"
					+ "		count: {$sum: 1}}}"
			,"{ $group: {_id: '$_id.id_region',\n"
			+ "	count: {$sum: '$count'},\n"
			+ "	datos: {$push: {estatus: '$_id.estatus',count: '$count'}}\n"
			+ "}}"
			,"{$unionWith: {\n"
					+ "  coll: 'cat_olts',\n"
					+ "  pipeline: [\n"
					+ "   {$group: { _id: '$id_region'}}\n"
					+ "  ]}\n"
					+ "}"
			,"{$group: {\n"
					+ "	_id: '$_id',\n"
					+ "	count: {$sum: '$count'},\n"
					+ "	datos: {$push: '$datos'}\n"
					+ "}}"
			,"{$unwind: {\n"
					+ "	path: '$datos',\n"
					+ "	preserveNullAndEmptyArrays: true\n"
					+ "}}"
			,"{$lookup: {\n"
					+ "  from: 'tb_inventario_onts',\n"
					+ "  'let': {region: '$_id'},\n"
					+ "  pipeline: [\n"
					+ "	{$match: {vip: 1}},\n"
					+ "	{$group: {_id: {id_olt: '$id_olt',region: '$id_region'}}},\n"
					+ "    {$lookup: {\n"
					+ "     from: 'cat_olts',\n"
					+ "     localField: '_id.id_olt',\n"
					+ "     foreignField: 'id_olt',\n"
					+ "     as: 'olt'\n"
					+ "	}},\n"
					+ "    {$set: {\n"
					+ "		id_olt: '$_id.id_olt',\n"
					+ "		region: '$_id.region',\n"
					+ "		estatus: {$ifNull: [{$arrayElemAt: ['$olt.estatus',0]},0]}\n"
					+ "    }},\n"
					+ "	{$unset: ['_id','olt']},\n"
					+ "    {$group: {_id: {id_region: '$region',estatus: '$estatus'},\n"
					+ "		count: {$sum: 1}\n"
					+ "    }},\n"
					+ "    {$group: {_id: '$_id.id_region',count: {$sum: '$count'},\n"
					+ "		datos: {$push: {\n"
					+ "			estatus: '$_id.estatus',\n"
					+ "			count: '$count'\n"
					+ "      }}\n"
					+ "    }},\n"
					+ "    {$set: {\n"
					+ "		activas: {$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},\n"
					+ "		inactivas: {$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}}\n"
					+ "	}},\n"
					+ "    {$set: {\n"
					+ "     activas: {$ifNull: [{$arrayElemAt: ['$activas.count',0]},0]},\n"
					+ "     inactivas: {$ifNull: [{$arrayElemAt: ['$inactivas.count',0]},0]}\n"
					+ "    }},\n"
					+ "   {$match: {$expr: {$eq: ['$_id','$$region']}}}\n"
					+ "  ],\n"
					+ "  as: 'olts'\n"
					+ "}}"
					,"{$lookup: {\n"
					+ "  from: 'tb_historico_diferencias',\n"
					+ "  'let': {region: '$_id'},\n"
					+ "  pipeline: [\n"
					+ "   {$match: {vip: 1}},\n"
					+ "   {$group: {\n"
					+ "		_id: '$id_region',\n"
					+ "		count: {$sum: 1}\n"
					+ "    }},\n"
					+ "   {$match: {$expr: {$eq: ['$_id','$$region']}}}\n"
					+ "  ],\n"
					+ "  as: 'cambios'\n"
					+ "}}"
					,"{$set: {\n"
					+ "    arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
					+ "	abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
					+ "	sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]},\n"
					+ "	totalActivas: {$ifNull: [{$arrayElemAt: ['$olts.activas',0]},0]},\n"
					+ "	totalInactivas: {$ifNull: [{$arrayElemAt: ['$olts.inactivas',0]},0]}\n"
					+ "}}"
					,"{$project: {\n"
					+ "	idRegion: '$_id',\n"
					+ "	region: {$concat: ['Región ',{$substr: ['$_id',0,-1]}]},\n"
					+ "	totalOlt: '$totalActivas',\n"
					+ "	totalRegion: {$add: ['$totalActivas','$totalInactivas']},\n"
					+ "	totalOnt: '$count',\n"
					+ "	arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
					+ "	abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
					+ "	sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]},\n"
					+ "	cambios: {$ifNull: [{$arrayElemAt: ['$cambios.count',0]},0]}}\n"
					+ "}"
					,"{$unset: ['_id']}"
					,"{$sort: {idRegion: 1}}"
	})
	List<datosRegionDto> getTotalesRegionesVips();

	
	@Query(value = " {'actualizacion': {$ne:null} ,'tipo':'E'}",count = true)
	Integer  findTotalCambiosE();
	
	@Query(value = " {'actualizacion': {$ne:null}}",count = true)
	Integer  findTotalCambiosT();
	
	@Aggregation(pipeline = {
	" {\n"
	+ "    '$match': {\n"
	+ "      'actualizacion': {\n"
	+ "        '$in': [\n"
	+ "          1, 2, 3, 4\n"
	+ "        ]\n"
	+ "      }\n"
	+ "    }\n"
	+ "  }"
	," {\n"
	+ "    '$unionWith': {\n"
	+ "      'coll': 'tb_inventario_onts_pdm'\n"
	+ "    }\n"
	+ "  } "
	,"{\n"
	+ "    '$project': {\n"
	+ "      'tipo': '$tipo', \n"
	+ "      'fecha_descubrimiento': '$fecha_descubrimiento', \n"
	+ "      'id_region': '$id_region', \n"
	+ "      'estatus': '$estatus', \n"
	+ "      'actualizacion': '$actualizacion', \n"
	+ "      'numero_serie': '$numero_serie'\n"
	+ "    }\n"
	+ "  } "
	,"{\n"
	+ "    '$sort': {\n"
	+ "      'id_region': 1\n"
	+ "    }\n"
	+ "  }"
	," {\n"
	+ "    '$limit':?1 \n"
	+ "  }"
	," {\n"
	+ "    '$skip':?0 \n"
	+ "  }"})
	@Meta(allowDiskUse = true)
	List<vwActualizacionEntidad> getDetalleActualizacionTSkip(@Param("skip") Integer skip,
			@Param("limit") Integer limit);

	@Aggregation(pipeline = {
			"{\n"
			+ "    '$match': {\n"
			+ "      'actualizacion': {\n"
			+ "        '$in': [\n"
			+ "          1, 2, 3, 4\n"
			+ "        ]\n"
			+ "      }\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$unionWith': {\n"
			+ "      'coll': 'tb_inventario_onts_pdm'\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$project': {\n"
			+ "      'tipo': '$tipo', \n"
			+ "      'fecha_descubrimiento': '$fecha_descubrimiento', \n"
			+ "      'id_region': '$id_region', \n"
			+ "      'estatus': '$estatus', \n"
			+ "      'actualizacion': '$actualizacion', \n"
			+ "      'numero_serie': '$numero_serie'\n"
			+ "    }\n"
			+ "  }"
			, " {\n"
			+ "    '$sort': {\n"
			+ "      'id_region': 1\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$limit': ?0\n"
			+ "  }" })
	@Meta(allowDiskUse = true)
	List<vwActualizacionEntidad> getDetalleActualizacionT(@Param("limit") Integer limit);

	@Query(value = "{}", count = true)
	Integer getTotalActualizadas();
	
	@Query(value = "{'tipo':'E'}", count = true)
	Integer getTotalActualizadasE();
	
	@Aggregation(pipeline = {
			"{\n"
			+ "    '$match': {\n"
			+ "      'actualizacion': {\n"
			+ "        '$in': [\n"
			+ "          1, 2, 3, 4\n"
			+ "        ]\n"
			+ "      }\n"
			+ "    }\n"
			+ "  }"
			, " {\n"
			+ "    '$unionWith': {\n"
			+ "      'coll': 'tb_inventario_onts_pdm'\n"
			+ "    }\n"
			+ "  }"
			, " {\n"
			+ "    '$project': {\n"
			+ "      'tipo': '$tipo', \n"
			+ "      'fecha_descubrimiento': '$fecha_descubrimiento', \n"
			+ "      'id_region': '$id_region', \n"
			+ "      'estatus': '$estatus', \n"
			+ "      'actualizacion': '$actualizacion', \n"
			+ "      'numero_serie': '$numero_serie'\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$sort': {\n"
			+ "      'id_region': 1\n"
			+ "    }\n"
			+ "  } "
			, "{\n"
			+ "    '$match': {\n"
			+ "      'tipo': 'E'\n"
			+ "    }\n"
			+ "  }"
			, " {\n"
			+ "    '$limit': ?1\n"
			+ "  }"
			, " {\n"
			+ "    '$skip': ?0\n"
			+ "  }"})
			@Meta(allowDiskUse = true)
			List<vwActualizacionEntidad> getDetalleActualizacionESkip(@Param("skip") Integer skip,
					@Param("limit") Integer limit);

			@Aggregation(pipeline = {
					" {\n"
					+ "    '$match': {\n"
					+ "      'actualizacion': {\n"
					+ "        '$in': [\n"
					+ "          1, 2, 3, 4\n"
					+ "        ]\n"
					+ "      }\n"
					+ "    }\n"
					+ "  } "
					, "{\n"
					+ "    '$unionWith': {\n"
					+ "      'coll': 'tb_inventario_onts_pdm'\n"
					+ "    }\n"
					+ "  } "
					, "{\n"
					+ "    '$project': {\n"
					+ "      'tipo': '$tipo', \n"
					+ "      'fecha_descubrimiento': '$fecha_descubrimiento', \n"
					+ "      'id_region': '$id_region', \n"
					+ "      'estatus': '$estatus', \n"
					+ "      'actualizacion': '$actualizacion', \n"
					+ "      'numero_serie': '$numero_serie'\n"
					+ "    }\n"
					+ "  }"
					, " {\n"
					+ "    '$sort': {\n"
					+ "      'id_region': 1\n"
					+ "    }\n"
					+ "  }, "
					, "{\n"
					+ "    '$match': {\n"
					+ "      'tipo': 'E'\n"
					+ "    }\n"
					+ "  }"
					, " {\n"
					+ "    '$limit': ?0\n"
					+ "  }" })
			@Meta(allowDiskUse = true)
			List<vwActualizacionEntidad> getDetalleActualizacionE(@Param("limit") Integer limit);
			
			
			@Aggregation(pipeline = {
					  " {$unionWith:\"tb_inventario_onts_pdm\"},\r\n"
					, "{\r\n"
					+ "        \"$group\": {\r\n"
					+ "                \"_id\": \"$id_olt\", \r\n"
					+ "                \"count\": { \"$sum\": 1 },      \r\n"
					+ "          },\r\n"
					+ "} \r\n"
					, "{\r\n"
					+ "		\"$lookup\":{\r\n"
					+ "			from: \"cat_olts\",\r\n"
					+ "			localField:\"_id\",\r\n"
					+ "			foreignField:\"id_olt\",\r\n"
					+ "			as: \"olts\",\r\n"
					+ "		}\r\n"
					+ "}\r\n"
					, "{$unwind:\"$olts\"}\r\n"
					, "{\r\n"
					+ "        $set:{\r\n"
					+ "            \"olts.onts\":\"$count\"\r\n"
					+ "        }\r\n"
					+ "}\r\n"
					, "{ $replaceRoot: { newRoot: \"$olts\" } }" })
			@Meta(allowDiskUse = true)
			List<responseOltsOntsDto> getOltsOnts();

	@Aggregation(pipeline = {
			"{$unionWith: 'tb_inventario_onts_pdm'}"
			," { '$group': {\n"
			+ "        '_id': {\n"
			+ "            'id_region': '$id_region',\n"
			+ "            'estatus': '$estatus',\n"
			+ "        },\n"
			+ "        'count': { '$sum': 1 },\n"
			+ "        \n"
			+ "    }}"
			," {'$group': {\n"
			+ "                '_id': '$_id.id_region',\n"
			+ "                 'count': { '$sum': '$count'  },\n"
			+ "                 'datos': { \n"
			+ "                    $push:{\n"
			+ "                        estatus:'$_id.estatus',\n"
			+ "                        count:'$count'\n"
			+ "                     \n"
			+ "                    }}                          \n"
			+ "          }\n"
			+ "    }"
			," {\n"
			+ "		'$lookup':{\n"
			+ "			from: 'tb_inventario_onts',\n"
			+ "			let: { region: '$_id'},\n"
			+ "			\n"
			+ "			pipeline: [\n"
			+ "                 {$match: { SA: true }},{ '$group': {\n"
			+ "                    '_id': {\n"
			+ "                        'id_region': '$id_region',\n"
			+ "                        'olts':'$id_olt'\n"
			+ "                    },        \n"
			+ "                }},\n"
			+ "                \n"
			+ "                {'$group': {\n"
			+ "                            '_id': '$_id.id_region',\n"
			+ "                             'count': { '$sum': 1  },                                                     \n"
			+ "                      }\n"
			+ "                },   			    \n"
			+ "			    { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'olts',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "		'$lookup':{\n"
			+ "			from: 'cat_olts',\n"
			+ "			let: { region: '$_id'},			\n"
			+ "			pipeline: [\n"
			+ "	            {\n"
			+ "                    '$group': {\n"
			+ "                            '_id': '$id_region', \n"
			+ "                            'count': { '$sum': 1 },      \n"
			+ "                      },\n"
			+ "               },                \n"
			+ "                			    \n"
			+ "			   { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'total',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "		'$lookup':{\n"
			+ "			from: 'tb_historico_diferencias',\n"
			+ "			let: { region: '$_id'},			\n"
			+ "			pipeline: [\n"
			+ "              {\n"
			+ "                    '$group': {\n"
			+ "                            '_id': '$id_region', \n"
			+ "                            'count': { '$sum': 1 },      \n"
			+ "                      },\n"
			+ "               },            \n"
			+ "                			    \n"
			+ "			   { $match:\n"
			+ "                 { $expr:\n"
			+ "                     { $eq: [ '$_id',  '$$region' ] }\n"
			+ "                 }\n"
			+ "              },\n"
			+ "			],\n"
			+ "			as: 'cambios',\n"
			+ "		}			\n"
			+ "	}"
			,"{\n"
			+ "	    $set:{\n"
			+ "            arriba : { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',1]}}} , 0 ]},\n"
			+ "            abajo :  { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',2]}}} , 0 ]},\n"
			+ "            sinInformacion: { $ifNull: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus', 0]}}} , 0 ]},\n"
			+ "            \n"
			+ "        }\n"
			+ "	}"
			,"{\n"
			+ "	    $project:\n"
			+ "           {\n"
			+ "             idRegion:'$_id',\n"
			+ "             region: { $concat: [ 'Región ', {$substr:['$_id', 0, -1 ]} ] },\n"
			+ "             totalOlt: { $ifNull: [  {$arrayElemAt : ['$olts.count',0]}, NumberInt(0)] },\n"
			+ "             totalRegion: { $ifNull: [  {$arrayElemAt : ['$total.count',0]}, NumberInt(0)] },\n"
			+ "             totalOnt: '$count',\n"
			+ "             arriba: { $ifNull: [  {$arrayElemAt : ['$arriba.count',0]}, NumberInt(0)] },\n"
			+ "             abajo: { $ifNull: [  {$arrayElemAt : ['$abajo.count',0]}, NumberInt(0)] }, \n"
			+ "             sinInformacion: { $ifNull: [  {$arrayElemAt : ['$sinInformacion.count',0]}, NumberInt(0)] }, \n"
			+ "             cambios: { $ifNull: [  {$arrayElemAt : ['$cambios.count',0]}, NumberInt(0)] }              \n"
			+ "          }\n"
			+ "          \n"
			+ "	}"
			,"{\n"
			+ "	    $set:{\n"
			+ "	        abajo:{$add: ['$sinInformacion', '$abajo']}\n"
			+ "	    }\n"
			+ "	        \n"
			+ "	}"
			,"{$unset: ['_id']}"
			,"{ $sort : { idRegion : 1 } }"
	})
	List<datosRegionDto> getTotalesByTecnologiaSA();
}

