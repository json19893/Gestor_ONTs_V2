package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.negocio.dto.responseMonitorMetricasManualInfoDto;
import totalplay.monitor.snmp.com.persistencia.entidad.MonitorPoleoManualEntity;



public interface ImonitorPoleoManualRepository extends MongoRepository<MonitorPoleoManualEntity, String> {

	MonitorPoleoManualEntity findFirstByOrderByIdDesc();
	@Aggregation(pipeline = {"{'$match':{_id: ObjectId(?0)} } "})
	MonitorPoleoManualEntity  getMonitorPoleo(@Param("id") String id);
	
	
	@Aggregation(pipeline = {
			  "{$sort:{_id:-1}}\n"
			, "{ $limit: 1 }\n"
			, " {\n"
			+ "         $set:{\n"
			+ "                 auxiliar: {$toString:\"$_id\"},\n"
			+ "             }\n"
			+ "}\n"
			, "{\n"
			+ "    		\"$lookup\":{\n"
			+ "    			from: \"monitor_poleo_metrica\",\n"
			+ "    			localField:\"auxiliar\",\n"
			+ "    			foreignField:\"id_poleo\", \n"
			+ "    			let: { poleo: \"$auxiliar\"},\n"
			+ "    			pipeline:[\n"
			+ "            	     {$match: {$expr: { $eq: [ \"$id_poleo\",  \"$$poleo\" ] } } },\n"
			+ "    			     {$match:{fecha_fin:{$ne:null}}},\n"
			+ "    			     {\"$group\": {\n"
			+ "                            \"_id\": \"$id_poleo\",                \n"
			+ "                            \"datos\": { $push:{metrica:\"$id_metrica\"}}                          \n"
			+ "                          }\n"
			+ "                    },{\n"
			+ "                        $set:{auxiliar:\"$_id\"}\n"
			+ "                    },{$unwind:\"$datos\"}\n"
			+ "    			    \n"
			+ "                    			   \n"
			+ "    			],   			\n"
			+ "    			as: \"terminados\",\n"
			+ "    		}\n"
			+ "}\n"
			, "{\n"
			+ "    		\"$lookup\":{\n"
			+ "    			from: \"monitor_poleo_metrica\",\n"
			+ "    			localField:\"auxiliar\",\n"
			+ "    			foreignField:\"id_poleo\", \n"
			+ "    			let: { poleo: \"$auxiliar\"},\n"
			+ "    			pipeline:[\n"
			+ "            	     {$match: {$expr: { $eq: [ \"$id_poleo\",  \"$$poleo\" ] } } },\n"
			+ "    			     {$match:{fecha_fin:{$eq:null}}},\n"
			+ "    			     {\"$group\": {\n"
			+ "                            \"_id\": \"$id_poleo\",                \n"
			+ "                            \"datos\": { $push:{metrica:\"$id_metrica\"}}                          \n"
			+ "                          }\n"
			+ "                    },{\n"
			+ "                        $set:{auxiliar:\"$_id\"}\n"
			+ "                    },{$unwind:\"$datos\"}\n"
			+ "    			    \n"
			+ "                    			   \n"
			+ "    			],   			\n"
			+ "    			as: \"procesando\",\n"
			+ "    		}\n"
			+ "}\n"
			, "{$set:{\n"
			+ "             terminados: {$ifNull:[\"$terminados.datos.metrica\", [-1]]},\n"
			+ "             procesando: {$ifNull:[\"$procesando.datos.metrica\",  [-1]]} \n"
			+ "         } \n"
			+ " }\n"
			, " {\n"
			+ "    		\"$lookup\":{\n"
			+ "    			from: \"tb_configuracion_metricas\",\n"
			+ "    			localField:\"bloque\",\n"
			+ "    			foreignField:\"bloque\",\n"
			+ "    			let: { metricas: {$concatArrays:[\"$procesando\", \"$terminados\"]}},\n"
			+ "    			pipeline:[\n"
			+ "    			    {$match:{ $expr: { $not: { $in: [ \"$id_metrica\", \"$$metricas\" ] } } } },\n"
			+ "    			],\n"
			+ "    			as: \"faltantes\",\n"
			+ "    		}\n"
			+ "}\n"
			, " {$set:{\n"
			+ "             faltantes: {$ifNull:[\"$faltantes.id_metrica\",[-1]]} ,\n"
			+ "}}"})
	List<responseMonitorMetricasManualInfoDto> getMonitorData();
}
