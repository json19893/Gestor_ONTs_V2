package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.negocio.dto.monitorPoleoDto;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.monitorPoleoEntidad;

public interface ImonitorPoleoRepositorio extends MongoRepository<monitorPoleoEntidad, String> {
	
	@Aggregation(pipeline={"{$match:{fecha_fin:{$ne:null}}}","{$sort:{_id:-1}}","{$limit:1}"})
	monitorPoleoEntidad getLastId();
	
	@Aggregation(pipeline = { "{$set: {_id: {$toString: '$_id'}}}"
			,"{\n"
					+ " $lookup: {\n"
					+ "  from: 'monitor_poleo_region',\n"
					+ "  localField: '_id',\n"
					+ "  foreignField: 'id_poleo',\n"
					+ "  pipeline: [\n"
					+ "   {$set: {_id: {$toString: '$_id'}}},\n"
					+ "   {\n"
					+ "    $lookup: {\n"
					+ "     from: 'monitor_poleo_region_hilo',\n"
					+ "     localField: '_id',\n"
					+ "     foreignField: 'id_region',\n"
					+ "     pipeline: [\n"
					+ "      {$set: {_id: {$toString: '$_id'}}},\n"
					+ "      {\n"
					+ "       $lookup: {\n"
					+ "        from: 'monitor_poleo_olt',\n"
					+ "        localField: '_id',\n"
					+ "        foreignField: 'id_region_hilo',\n"
					+ "        pipeline: [\n"
					+ "         {$set: {_id: {$toString: '$_id'}}},\n"
					+ "         {\n"
					+ "          $lookup: {\n"
					+ "           from: 'monitor_poleo_olt_metrica',\n"
					+ "           localField: '_id',\n"
					+ "           foreignField: 'id_poleo_olt',\n"
					+ "           pipeline: [\n"
					+ "            {$set: {_id: {$toString: '$_id'}}}\n"
					+ "           ],\n"
					+ "           as: 'metricas'\n"
					+ "          }\n"
					+ "         }\n"
					+ "        ],\n"
					+ "        as: 'poleo_olt'\n"
					+ "       }\n"
					+ "      }\n"
					+ "     ],\n"
					+ "     as: 'poleo_region_hilo'\n"
					+ "    }\n"
					+ "   }\n"
					+ "  ],\n"
					+ "  as: 'poleo_region'\n"
					+ " }\n"
					+ "}"
			
	})
	monitorPoleoDto getInfoPoelo();

}
