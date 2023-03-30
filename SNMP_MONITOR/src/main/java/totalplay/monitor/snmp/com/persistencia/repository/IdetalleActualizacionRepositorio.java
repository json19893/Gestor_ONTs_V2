package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.persistencia.entidad.detalleActualizacionesEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;


@Repository
public interface IdetalleActualizacionRepositorio extends MongoRepository<detalleActualizacionesEntidad, String> {
	@Aggregation(pipeline = { 
			  "{$match:{$expr:{$gte:['$fechaActualizacion',  {$dateSubtract:{ startDate: new Date('2023-02-03T02:00:00.000Z'), unit: 'hour', amount: ?0}}  ]}}}\r\n"
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
	List<detalleActualizacionesEntidad> getDetalleEmpresariales(@Param("hours") Integer hours);
	
	@Aggregation(pipeline = { 
			 "{$match:{$expr:{$gte:['$fechaActualizacion',  {$dateSubtract:{ startDate: new Date('2023-02-03T02:00:00.000Z'), unit: 'hour', amount: ?0}}  ]}}}\r\n"
	})
	List<detalleActualizacionesEntidad> getDetalle(@Param("hours") Integer hours);
	
}