package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasEntity;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IdiferenciasRepository extends MongoRepository<DiferenciasEntity, String> {
	@Aggregation(pipeline = { 
		      "{\n"
		    + "\"$group\": {\n"
		    + "        \"_id\": \"$numero_serie\", \n"
		    + "        \"count\": { \"$sum\": 1 },\n"
		    + "        \"datos\": { \n"
		    + "            $push:{\n"
		    + "                _id:\"$_id\",\n"
		    + "                estatus:\"$estatus\"\n"
		    + "            }}        \n"
		    + "  }\n"
		    + "}\n"
		    , "{\n"
		    + "  $set:{\n"
		    + "    id_ont:{\n"
		    + "			$cond: [ { $eq: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',0]}  }}, [] ] },\n"
		    + "                         {$filter: {input: '$datos', cond: {$or:[{$eq: ['$$this.estatus',0]},{$eq: ['$$this.estatus',1]},{$eq: ['$$this.estatus',2]}]} }},\n"
		    + "                         {$filter: {input: '$datos', cond: {$or:[{$eq: ['$$this.estatus',1]},{$eq: ['$$this.estatus',2]}]} }}  \n"
		    + "                 ]\n"
		    + "    }            \n"
		    + "  }\n"
		    + "}\n"
		    , "{\n"
		    + "  $match: {\n"
		    + "    $expr: {\n"
		    + "        $cond: {\n"
		    + "            if: true,\n"
		    + "            then: {$eq: [{$size: \"$id_ont\"}, 1]},\n"
		    + "            else: {$gt: [{$size: \"$id_ont\"}, 1]}\n"
		    + "        }\n"
		    + "    }\n"
		    + "  }\n"
		    + "}\n"
		    , "{$unwind:\"$id_ont\"}\n"
		    , "{\n"
		    + "	\"$lookup\":{\n"
		    + "		from: \"tb_inventario_onts_tmp\",\n"
		    + "		localField:\"id_ont._id\",\n"
		    + "		foreignField:\"_id\",\n"
		    + "		as: \"inventario\",\n"
		    + "	}\n"
		    + "}\n"
		    , "{\"$unwind\": \"$inventario\"}\n"
		    , "{ $replaceRoot: { newRoot: \"$inventario\" } }"})
	List<InventarioOntsAuxEntity> findDiferencias();
	
	@Aggregation(pipeline = { 
		      "{\n"
		    + "\"$group\": {\n"
		    + "        \"_id\": \"$numero_serie\", \n"
		    + "        \"count\": { \"$sum\": 1 },\n"
		    + "        \"datos\": { \n"
		    + "            $push:{\n"
		    + "                _id:\"$_id\",\n"
		    + "                estatus:\"$estatus\"\n"
		    + "            }}        \n"
		    + "  }\n"
		    + "}\n"
		    , "{\n"
		    + "  $set:{\n"
		    + "    id_ont:{\n"
		    + "			$cond: [ { $eq: [ {$filter: {input: '$datos', cond: {$eq: ['$$this.estatus',0]}  }}, [] ] },\n"
		    + "                         {$filter: {input: '$datos', cond: {$or:[{$eq: ['$$this.estatus',0]},{$eq: ['$$this.estatus',1]},{$eq: ['$$this.estatus',2]}]} }},\n"
		    + "                         {$filter: {input: '$datos', cond: {$or:[{$eq: ['$$this.estatus',1]},{$eq: ['$$this.estatus',2]}]} }}  \n"
		    + "                 ]\n"
		    + "    }            \n"
		    + "  }\n"
		    + "}\n"
		    , "{\n"
		    + "  $match: {\n"
		    + "    $expr: {\n"
		    + "        $cond: {\n"
		    + "            if: false,\n"
		    + "            then: {$eq: [{$size: \"$id_ont\"}, 1]},\n"
		    + "            else: {$gt: [{$size: \"$id_ont\"}, 1]}\n"
		    + "        }\n"
		    + "    }\n"
		    + "  }\n"
		    + "}\n"
		    , "{$unwind:\"$id_ont\"}\n"
		    , "{\n"
		    + "	\"$lookup\":{\n"
		    + "		from: \"tb_inventario_onts_tmp\",\n"
		    + "		localField:\"id_ont._id\",\n"
		    + "		foreignField:\"_id\",\n"
		    + "		as: \"inventario\",\n"
		    + "	}\n"
		    + "}\n"
		    , "{\"$unwind\": \"$inventario\"}\n"
		    , "{ $replaceRoot: { newRoot: \"$inventario\" } }"})
	List<DiferenciasManualEntity> findDiferenciasManual();
	
}
