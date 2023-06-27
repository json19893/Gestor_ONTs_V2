package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;
import java.util.Date;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.negocio.dto.AceptadasNCEDto;
import totalplay.monitor.snmp.com.negocio.dto.InventarioRechazadoNCEDto;
import totalplay.monitor.snmp.com.negocio.dto.dataRegionResponseDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesTecnologiaDto;
import totalplay.monitor.snmp.com.persistencia.entidad.InventarioRechazadasNCEEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;

public interface IinventarioRechazadosNCERepositorio extends MongoRepository<InventarioRechazadasNCEEntidad, String> {
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'tecnologia':?0},{'estatus':?1}]}}" })
	List<catOltsEntidad> findCatOltsByStatus(@Param("tipo") String tipo,@Param("estatus") Integer estatus );
	
	
	@Aggregation(pipeline = { 
			  "{$match:{ip:?0}}\r\n"
			, "{$match: {$and: [{fechaActualizacion:{$gt: ?2 }},  {fechaActualizacion:{$lte: ?3 }} ] }}\r\n"
			, "{ \"$group\": {\r\n"
			+ "        \"_id\": {\r\n"
			+ "            \"frame\": \"$frame\",\r\n"
			+ "            \"slot\" : \"$slot\",\r\n"
			+ "            \"port\" : \"$port\",\r\n"
			+ "            \"uid\"  : \"$uid\"\r\n"
			+ "        },\r\n"
			+ "        onts: { $last:\"$$ROOT\"}\r\n"
			+ "}}\r\n"
			, "{\r\n"
			+ "        $set:{\r\n"
			+ "            \"onts.id_olt\":?1,\r\n"
			+ "            \"onts.fecha_descubrimiento\":\"$onts.fechaActualizacion\",\r\n"
			+ "            \"onts.numero_serie\": { $cond: [ {$eq :[ \"$onts.numeroSerie\",  \"na\"  ]},  null, \"$onts.numeroSerie\"] },\r\n"
			+ "            \r\n"
			+ "        }\r\n"
			+ " }\r\n"
			, " {\r\n"
			+ "        $unset:[ \"onts.fechaActualizacion\", \"onts.numeroSerie\", \"onts.causa\", \"onts.ip\" ]\r\n"
			+ " }\r\n"
			, " { $replaceRoot: { newRoot: \"$onts\" } }   \r\n"
			, " { $unionWith: { coll: \"tb_inventario_onts_pdm\", pipeline: [ {$match: {id_olt:1}} ]} }" 
			})
	List<inventarioOntsEntidad> getRechazadasNCE(@Param("ip") String ip, @Param("olt") Integer olt, @Param("fechInicial") Date fechaInicial,  @Param("fechaFinal") Date fechaFinal );
	
	@Aggregation(pipeline = { 
			    "{$match:{ip:?0}},\r\n"
			  , "{$match: {$and: [{fechaActualizacion:{$gt: ?2 }},  {fechaActualizacion:{$lte: ?3 }} ] }}\r\n"
			  , "{ \"$group\": {\r\n"
			  + "        \"_id\": {\r\n"
			  + "            \"frame\": \"$frame\",\r\n"
			  + "            \"slot\" : \"$slot\",\r\n"
			  + "            \"port\" : \"$port\",\r\n"
			  + "            \"uid\"  : \"$uid\"\r\n"
			  + "        },\r\n"
			  + "        onts: { $last:\"$$ROOT\"}\r\n"
			  + "}}\r\n"
			  , "{\r\n"
			  + "        $set:{\r\n"
			  + "            \"onts.id_olt\":?1,\r\n"
			  + "            \"onts.fecha_descubrimiento\":\"$onts.fechaActualizacion\",\r\n"
			  + "            \"onts.numero_serie\": { $cond: [ {$eq :[ \"$onts.numeroSerie\",  \"na\"  ]},  null, \"$onts.numeroSerie\"] },\r\n"
			  + "            \r\n"
			  + "        }\r\n"
			  + "}\r\n"
			  , "{\r\n"
			  + "        $unset:[ \"onts.fechaActualizacion\", \"onts.numeroSerie\", \"onts.causa\", \"onts.ip\" ]\r\n"
			  + "}\r\n"
			  , "{ $replaceRoot: { newRoot: \"$onts\" } },   \r\n"
			  , "{ $unionWith: { coll: \"tb_inventario_onts_pdm\", pipeline: [ {$match: {id_olt:?1}} ]} }\r\n"
			  , "{\r\n"
			  + "		\"$lookup\":{\r\n"
			  + "			from: \"tb_inventario_onts_descubrimiento_nce\",\r\n"
			  + "			localField:\"id_olt\",\r\n"
			  + "			foreignField:\"id_olt\",\r\n"
			  + "		    let: { olt: \"$id_olt\", frame:\"$frame\", slot:\"$slot\", port:\"$port\", uid:\"$uid\", serie:\"$numero_serie\"},\r\n"
			  + "			pipeline: [\r\n"
			  + "				{$match: {id_ejecucion: ?4 } },"
			  + "			    { $match: { $expr:{ $eq: [ \"$id_olt\",  \"$$olt\" ] } }},\r\n"
			  + "			    { $match:\r\n"
			  + "                    { $expr:\r\n"
			  + "                        {$or:[\r\n"
			  + "                             { $and:[ \r\n"
			  + "                                { $eq: [ \"$frame\", \"$$frame\" ] },\r\n"
			  + "                                { $eq: [ \"$slot\", \"$$slot\" ] },\r\n"
			  + "                                { $eq: [ \"$port\", \"$$port\" ] },\r\n"
			  + "                                { $eq: [ \"$uid\", \"$$uid\" ] },\r\n"
			  + "                            ]},\r\n"
			  + "                             { $eq: [ \"$numero_serie\", \"$$serie\" ] }\r\n"
			  + "                         \r\n"
			  + "                         ]}\r\n"
			  + "                     }\r\n"
			  + "                }\r\n"
			  + "			   \r\n"
			  + "			],\r\n"
			  + "			as: \"nce\",\r\n"
			  + "		}\r\n"
			  + " }\r\n"
//			  , " {\r\n"
//			  + "        $set:{\r\n"
//			  + "            inventario: { $cond: [  {$eq :[ \"$nce\", []  ]}, false, true ] },\r\n"
//			  + "            \r\n"
//			  + "        }\r\n"
//			  + " }"
			  , "{$match:{nce:{$ne: []}}}"	
			  , "{$unwind: \"$nce\"}\r\n"
			  , "{\r\n"
			  + "        $set :{\r\n"
			  + "             oid:\"$nce.oid\",\r\n"
			  + "            frame:\"$nce.frame\",\r\n"
			  + "            slot:\"$nce.slot\",\r\n"
			  + "            port:\"$nce.port\",\r\n"
			  + "            id_ejecucion:\"$nce.id_ejecucion\",\r\n"
			  + "        }\r\n"
			  + " }"
			  , "{\r\n"
			  + "        $unset:[\"nce\"]\r\n"
			  + "}   "
			  //, "{$match: {inventario:true}}" 
			  })
	List<AceptadasNCEDto> getRechazadasNCEInventario(@Param("ip") String ip, @Param("olt") Integer olt, @Param("fechInicial") Date fechaInicial,  @Param("fechaFinal") Date fechaFinal, @Param("ejecucion") String ejecucion );
	
}
