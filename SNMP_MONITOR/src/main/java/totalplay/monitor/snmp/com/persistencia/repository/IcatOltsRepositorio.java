package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.negocio.dto.totalesTecnologiaDto;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;

public interface IcatOltsRepositorio extends MongoRepository<catOltsEntidad, String> {
	catOltsEntidad findByIp(String ip);	
	catOltsEntidad findFirstByOrderByIdDesc();
	@Aggregation(pipeline = { "{'$match':{'$and':[{'tecnologia':?0},{'estatus':?1}]}}" })
	List<catOltsEntidad> findCatOltsByStatus(@Param("tipo") String tipo,@Param("estatus") Integer estatus );
	
	@Aggregation(pipeline = { "{'$match':{'estatus':?0}}" })
	List<catOltsEntidad> findCatOltsByEstatus(@Param("estatus") Integer estatus );
	
	@Aggregation(pipeline = { "{'$match':{'tecnologia':?0}}" })
	List<catOltsEntidad>  findCatOltsByTecnologia(@Param("tecnologia") String tecnologia );

	
	@Aggregation(pipeline = { "{'$match':{'id_region':?0}}" })
	List<catOltsEntidad>  findCatOltsRegion(@Param("idRegion") Integer idRegion);
	
	@Query(value="{'id_region': ?0, 'estatus': 1}",count=true)
	Integer findCatActivos(@Param("idRegion") Integer idRegion );
	
	@Query(value="{'id_region': ?0}",count=true)
	Integer findCaTotales(@Param("idRegion") Integer idRegion );
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'id_region':?0},{'tecnologia':?1},{'estatus':?2}]}}" })
	List<catOltsEntidad> findCatOltsByStatusTecnologiaRegion(@Param("region") Integer region, @Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus );
	
	@Query(value="{'tecnologia': ?0, 'estatus': ?1}",count=true)
	Integer findCatOltsByStatusCount(@Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus );

	@Query(value="{'id_region':?0 ,'tecnologia':?1,'estatus':?2}",count=true)
	Integer findCatOltsByStatusTecnologiaRegionCount(@Param("region") Integer region, @Param("tecnologia") String tecnologia, @Param("estatus") Integer estatus );
	
	@Query(value="{'tecnologia':?0}",count=true)
	Integer findCatOltsByTecnologiaCount(@Param("tecnologia") String tecnologia );
	
	@Aggregation(pipeline = { "{'$match':{'ip':?0}}" })
	catOltsEntidad  findOltByIp(@Param("ip") String ip);
	
	@Aggregation(pipeline = { "{'$match':{'nombre':?0}}" })
	catOltsEntidad  findOltByNombre(@Param("nombre") String nombre);
	
	@Aggregation(pipeline = { "{'$match':{'id_olt':?0}}" })
	catOltsEntidad  findOltByIdolt(@Param("id_olt") Integer idOlt);
	
	@Query(value="{'nombre': {$regex: ?0, $options:'i'}}" )
	List<catOltsEntidad> findNombreByRegex(@Param("regex") String regex);
	
	@Query(value="{'ip': {$regex: ?0, $options:'i'}}" )
	List<catOltsEntidad> findIpByRegex(@Param("regex") String regex);
		
	@Aggregation(pipeline = {
			"{\n"
			+ " $lookup: {\n"
			+ "  from: 'tb_inventario_onts',\n"
			+ "  'let': {olt: '$id_olt'},\n"
			+ "  pipeline: [\n"
			+ "   {$match: {tipo: 'E'}},\n"
			+ "   {$group: {\n"
			+ "		_id: '$id_olts',\n"
			+ "		count: {$sum: 1}\n"
			+ "    }\n"
			+ "   },\n"
			+ "   {$match: {\n"
			+ "	$expr: {\n"
			+ "      $eq: ['$_id','$$olt']\n"
			+ "     }\n"
			+ "    }\n"
			+ "   }\n"
			+ "  ],\n"
			+ "  as: 'total'\n"
			+ " }\n"
			+ "}"
			,"{$match: {total: {$ne: []}}}"
			,"{$group: {\n"
			+ "  _id: {\n"
			+ "   tecnologia: '$tecnologia',\n"
			+ "   estatus: '$estatus'\n"
			+ "  },\n"
			+ "  count: {$sum: 1}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $group: {\n"
			+ "  _id: '$_id.tecnologia',\n"
			+ "  total: {$sum: '$count'},\n"
			+ "  datos: {\n"
			+ "   $push: {\n"
			+ "    estatus: '$_id.estatus',\n"
			+ "    count: '$count'\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
			+ "  abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  tecnologia: '$_id',\n"
			+ "  arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
			+ "  abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  abajo: {$add: ['$abajo','$sinInformacion']}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $unset: ['datos','sinInformacion','_id']\n"
			+ "}"
			
	})
	List<totalesTecnologiaDto> getTotalesTecnologia();
	
	
	@Aggregation(pipeline = {
			"{\n"
			+ " $lookup: {\n"
			+ "  from: 'tb_inventario_onts',\n"
			+ "  'let': {olt: '$id_olt'},\n"
			+ "  pipeline: [\n"
			+ "   {$group: {\n"
			+ "		_id: '$id_olts',\n"
			+ "		count: {$sum: 1}\n"
			+ "    }\n"
			+ "   },\n"
			+ "   {$match: {\n"
			+ "	$expr: {\n"
			+ "      $eq: ['$_id','$$olt']\n"
			+ "     }\n"
			+ "    }\n"
			+ "   }\n"
			+ "  ],\n"
			+ "  as: 'total'\n"
			+ " }\n"
			+ "}"
			,"{$group: {\n"
			+ "  _id: {\n"
			+ "   tecnologia: '$tecnologia',\n"
			+ "   estatus: '$estatus'\n"
			+ "  },\n"
			+ "  count: {$sum: 1}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $group: {\n"
			+ "  _id: '$_id.tecnologia',\n"
			+ "  total: {$sum: '$count'},\n"
			+ "  datos: {\n"
			+ "   $push: {\n"
			+ "    estatus: '$_id.estatus',\n"
			+ "    count: '$count'\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
			+ "  abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  tecnologia: '$_id',\n"
			+ "  arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
			+ "  abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  abajo: {$add: ['$abajo','$sinInformacion']}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $unset: ['datos','sinInformacion','_id']\n"
			+ "}"
			
	})
	List<totalesTecnologiaDto> getTotalesTecnologiaT();


	@Aggregation(pipeline = {
			"{$match:{id_region:?0}}"
			,"{\n"
			+ " $lookup: {\n"
			+ "  from: 'tb_inventario_onts',\n"
			+ "  'let': {olt: '$id_olt'},\n"
			+ "  pipeline: [\n"
			+ "   {$unionWith: 'tb_inventario_onts_pdm'},\n"
			+ "   {$match: {'vip': 1}},\n"
			+ "   {$group: {\n"
			+ "		_id: '$id_olts',\n"
			+ "		count: {$sum: 1}\n"
			+ "    }\n"
			+ "   },\n"
			+ "   {$match: {\n"
			+ "	$expr: {\n"
			+ "      $eq: ['$_id','$$olt']\n"
			+ "     }\n"
			+ "    }\n"
			+ "   }\n"
			+ "  ],\n"
			+ "  as: 'total'\n"
			+ " }\n"
			+ "}"
			,"{$match: {total: {$ne: []}}}"
			,"{$group: {\n"
			+ "  _id: {\n"
			+ "   tecnologia: '$tecnologia',\n"
			+ "   estatus: '$estatus'\n"
			+ "  },\n"
			+ "  count: {$sum: 1}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $group: {\n"
			+ "  _id: '$_id.tecnologia',\n"
			+ "  total: {$sum: '$count'},\n"
			+ "  datos: {\n"
			+ "   $push: {\n"
			+ "    estatus: '$_id.estatus',\n"
			+ "    count: '$count'\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
			+ "  abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  tecnologia: '$_id',\n"
			+ "  arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
			+ "  abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  abajo: {$add: ['$abajo','$sinInformacion']}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $unset: ['datos','sinInformacion','_id']\n"
			+ "}"
			
	})
	List<totalesTecnologiaDto> getTotalesTecnologiaRegionVips(@Param("region") Integer region);
	
	@Aggregation(pipeline = {
			"{$match:{id_region:?0}}"
			,"{\n"
			+ " $lookup: {\n"
			+ "  from: 'tb_inventario_onts',\n"
			+ "  'let': {olt: '$id_olt'},\n"
			+ "  pipeline: [\n"
			+ "   {$unionWith: 'tb_inventario_onts_pdm'},\n"
			+ "   {$match: {tipo: 'E'}},\n"
			+ "   {$group: {\n"
			+ "		_id: '$id_olts',\n"
			+ "		count: {$sum: 1}\n"
			+ "    }\n"
			+ "   },\n"
			+ "   {$match: {\n"
			+ "	$expr: {\n"
			+ "      $eq: ['$_id','$$olt']\n"
			+ "     }\n"
			+ "    }\n"
			+ "   }\n"
			+ "  ],\n"
			+ "  as: 'total'\n"
			+ " }\n"
			+ "}"
			,"{$match: {total: {$ne: []}}}"
			,"{$group: {\n"
			+ "  _id: {\n"
			+ "   tecnologia: '$tecnologia',\n"
			+ "   estatus: '$estatus'\n"
			+ "  },\n"
			+ "  count: {$sum: 1}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $group: {\n"
			+ "  _id: '$_id.tecnologia',\n"
			+ "  total: {$sum: '$count'},\n"
			+ "  datos: {\n"
			+ "   $push: {\n"
			+ "    estatus: '$_id.estatus',\n"
			+ "    count: '$count'\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
			+ "  abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  tecnologia: '$_id',\n"
			+ "  arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
			+ "  abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  abajo: {$add: ['$abajo','$sinInformacion']}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $unset: ['datos','sinInformacion','_id']\n"
			+ "}"
			
	})
	List<totalesTecnologiaDto> getTotalesTecnologiaRegion(@Param("region") Integer region);
	
	@Aggregation(pipeline = {
			"{\n"
			+ " $lookup: {\n"
			+ "  from: 'tb_inventario_onts',\n"
			+ "  'let': {olt: '$id_olt'},\n"
			+ "  pipeline: [\n"
			+ "   {$match: {vip: 1}},\n"
			+ "   {$group: {\n"
			+ "		_id: '$id_olts',\n"
			+ "		count: {$sum: 1}\n"
			+ "    }\n"
			+ "   },\n"
			+ "   {$match: {\n"
			+ "	$expr: {\n"
			+ "      $eq: ['$_id','$$olt']\n"
			+ "     }\n"
			+ "    }\n"
			+ "   }\n"
			+ "  ],\n"
			+ "  as: 'total'\n"
			+ " }\n"
			+ "}"
			,"{$match: {total: {$ne: []}}}"
			,"{$group: {\n"
			+ "  _id: {\n"
			+ "   tecnologia: '$tecnologia',\n"
			+ "   estatus: '$estatus'\n"
			+ "  },\n"
			+ "  count: {$sum: 1}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $group: {\n"
			+ "  _id: '$_id.tecnologia',\n"
			+ "  total: {$sum: '$count'},\n"
			+ "  datos: {\n"
			+ "   $push: {\n"
			+ "    estatus: '$_id.estatus',\n"
			+ "    count: '$count'\n"
			+ "   }\n"
			+ "  }\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  arriba: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',1]}}},0]},\n"
			+ "  abajo: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',2]}}},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$filter: {input: '$datos',cond: {$eq: ['$$this.estatus',0]}}},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  tecnologia: '$_id',\n"
			+ "  arriba: {$ifNull: [{$arrayElemAt: ['$arriba.count',0]},0]},\n"
			+ "  abajo: {$ifNull: [{$arrayElemAt: ['$abajo.count',0]},0]},\n"
			+ "  sinInformacion: {$ifNull: [{$arrayElemAt: ['$sinInformacion.count',0]},0]}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $set: {\n"
			+ "  abajo: {$add: ['$abajo','$sinInformacion']}\n"
			+ " }\n"
			+ "}"
			,"{\n"
			+ " $unset: ['datos','sinInformacion','_id']\n"
			+ "}"
			
	})
	List<totalesTecnologiaDto> getTotalesTecnologiaVips();

}
