package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.detalleActualizacionesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;


@Repository
public interface IdetalleActualizacionRepositorio extends MongoRepository<detalleActualizacionesEntidad, String> {
	@Aggregation(pipeline = { 
			  "{$match:{$expr:{$gte:['$fechaActualizacion',  ?0  ]}}}\r\n"
			, "{\r\n"
			+ "	'$group': {\r\n"
			+ "		'_id': '$numeroSerie', \r\n"
			+ "		datos:{$last:'$$ROOT'}\r\n"
			+ "			   \r\n"
			+ "	},\r\n"
			+ "}\r\n"
			, "{ $replaceRoot: { newRoot: '$datos' } }\r\n"
			, "{\r\n"
			+ "	'$lookup':{\r\n"
			+ "		from: 'tb_inventario_onts',\r\n"
			+ "		localField:'numeroSerie',\r\n"
			+ "		foreignField:'numero_serie',\r\n"
			+ "		pipeline:[ {$match:{tipo:'E'}} ],\r\n"
			+ "		as: 'ont',\r\n"
			+ "	}\r\n"
			+ "}\r\n"
			, "{$match:{ont:{$ne:[]}}}" 
			})
	@Meta(allowDiskUse = true)
	List<detalleActualizacionesEntidad> getDetalleEmpresariales(@Param("date") Date date);
	
	
	
		@Aggregation(pipeline = { 
				  "{$match:{$expr:{$gte:['$fechaActualizacion',  ?0  ]}}}\r\n"
				, "{\r\n"
				+ "	'$group': {\r\n"
				+ "		'_id': '$numeroSerie', \r\n"
				+ "		datos:{$last:'$$ROOT'}\r\n"
				+ "			   \r\n"
				+ "	},\r\n"
				+ "}\r\n"
				, "{ $replaceRoot: { newRoot: '$datos' } }\r\n"
				, "{\r\n"
				+ "	'$lookup':{\r\n"
				+ "		from: 'tb_inventario_onts',\r\n"
				+ "		localField:'numeroSerie',\r\n"
				+ "		foreignField:'numero_serie',\r\n"
				+ "		pipeline:[ {$match:{vip:1}} ],\r\n"
				+ "		as: 'ont',\r\n"
				+ "	}\r\n"
				+ "}\r\n"
				, "{$match:{ont:{$ne:[]}}}" 
				})
		@Meta(allowDiskUse = true)
	List<detalleActualizacionesEntidad> getDetalleVips(@Param("date") Date date);

	@Aggregation(pipeline = {
			"{$match:{$expr:{$gte:['$fechaActualizacion',  ?0  ]}}}\r\n"
			, "{$match:{sa:true}},{\r\n"
			+ "	'$group': {\r\n"
			+ "		'_id': '$numeroSerie', \r\n"
			+ "		datos:{$last:'$$ROOT'}\r\n"
			+ "			   \r\n"
			+ "	},\r\n"
			+ "}\r\n"
			, "{ $replaceRoot: { newRoot: '$datos' } }\r\n"
			, "{\r\n"
			+ "	'$lookup':{\r\n"
			+ "		from: 'tb_inventario_onts',\r\n"
			+ "		localField:'numeroSerie',\r\n"
			+ "		foreignField:'numero_serie',\r\n"
			+ "		pipeline:[ {$match:{sa:true}} ],\r\n"
			+ "		as: 'ont',\r\n"
			+ "	}\r\n"
			+ "}\r\n"
			, "{$match:{ont:{$ne:[]}}}"
	})
	@Meta(allowDiskUse = true)
	List<detalleActualizacionesEntidad> getDetalleSA(@Param("date") Date date);



	@Query("{fechaActualizacion: {$gte: '2023-06-10T22:00:11.186'}}")
	List<detalleActualizacionesEntidad> getDetalle(@Param("date") Date date);
	
	@Aggregation(pipeline = { 
			     "{$match:{numeroSerie: ?0 }}\r\n"
			   , "{\r\n"
			   + "	\"$group\": {\r\n"
			   + "		\"_id\": \"$numeroSerie\", \r\n"
			   + "		\"numeroSerie\":{$last:\"$numeroSerie\"},\r\n"
			   + "		\"traza\": {$push: \"$$ROOT\"}, \r\n"
			   + "	 },\r\n"
			   + "}\r\n"
			   , "{\r\n"
			   + "	\"$lookup\":{\r\n"
			   + "		from: \"tb_inventario_onts\",\r\n"
			   + "		localField:\"numeroSerie\",\r\n"
			   + "		foreignField:\"numero_serie\",\r\n"
			   + "		pipeline:[  {$match:{tipo:\"E\"}}  ],\r\n"
			   + "		as: \"inventario\",\r\n"
			   + "	}\r\n"
			   + "}\r\n"
			   , "{    \r\n"
			   + "	$project:{\r\n"
			   + "	    traza: { $cond: [ {$eq: [{$size: \"$inventario\"}, 1]}, \"$traza\", [] ] }\r\n"
			   + "	}\r\n"
			   + "}\r\n"
			   , "{$unwind: \"$traza\"}\r\n"
			   , "{ $replaceRoot: { newRoot: \"$traza\" } }"
	})		
	List<detalleActualizacionesEntidad> getDetalleBySerieEmp(@Param("serie") String serie);
	
	
	@Aggregation(pipeline = { 
		     "{$match:{numeroSerie: ?0 }}\r\n"
	})		
	List<detalleActualizacionesEntidad> getDetalleBySerie(@Param("serie") String serie);
}