package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.LimpiezaManualDto;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsTempNCERepository extends MongoRepository<InventarioOntsTmpNCEEntity, String> {
	
	@Query("{numero_serie: ?0}")
	InventarioOntsEntity finOntSerie(String numSerie);
   
	
	@Aggregation(pipeline = { 
		      "{$match:{id_olt:?1}}\n"
			, "{\n"
	  		+ "        $lookup:{\n"
	  		+ "            from: \"auxiliar_descubrimiento_nce\",\n"
	  		+ "            localField:\"index\",\n"
	  		+ "            foreignField: \"index\",          \n"
	  		+ "            as:\"metrica\"\n"
	  		+ "        \n"
	  		+ "        }    \n"
	  		+ " }\n"
	  		, "{\n"
	  		+ "        $lookup:{\n"
	  		+ "            from: \"tb_inventario_onts\",\n"
	  		+ "            localField:\"index\",\n"
	  		+ "            foreignField: \"index\",          \n"
	  		+ "            as:\"inventario\"\n"
	  		+ "        \n"
	  		+ "        }    \n"
	  		+ " }\n"
	  		, " {\n"
	  		+ "        $set:{\n"
	  		+ "            estatus:{$ifNull:[{$cond: [ { $eq: [ {$arrayElemAt : ['$metrica.estatus',0]}, 0 ] },null, {$arrayElemAt : ['$metrica.estatus',0]}  ]}, {$arrayElemAt : ['$inventario.estatus',0]}, 0 ]}\n"
	  		+ "        }\n"
	  		+ "}\n"
	  		, "{$unset:[\"metrica\", '_id', 'inventario']}\n"
	  		, "{$out: 'tb_inventario_onts_tmp_nce'}\n"
	  		})
	List<InventarioOntsTmpNCEEntity> getEstatusNCE(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	
	@Aggregation(pipeline = { 
		      "{$match:{id_olt:?1}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar_descubrimiento_nce\",\n"
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
			, "{$out: 'tb_inventario_onts_tmp_nce'}\n"
			})
	List<InventarioAuxTransEntity> getDescripcionAlarma(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	@Aggregation(pipeline = { 
		      "{$match:{id_olt:?1}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar_descubrimiento_nce\",\n"
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
			, "{$out: 'tb_inventario_onts_tmp_nce'}\n"
			})
	List<InventarioAuxTransEntity> getLastDownTime(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	@Aggregation(pipeline = { 
		      "{$match:{id_olt:?1}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar_descubrimiento_nce\",\n"
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
			, "{$out: 'tb_inventario_onts_tmp_nce'}\n"
			})
	List<InventarioAuxTransEntity> getAlias(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	@Aggregation(pipeline = { 
		      "{$match:{id_olt:?1}}\n"
			, "{\n"
			+ "        $lookup:{\n"
			+ "            from: \"auxiliar_descubrimiento_nce\",\n"
			+ "            localField:\"indexFSP\",\n"
			+ "            foreignField: \"indexFSP\",          \n"
			+ "            as:\"metrica\"\n"
			+ "        \n"
			+ "        }    \n"
			+ " }\n"
			, " {\n"
			+ "        $set:{\n"
			+ "            frame:{$ifNull:[{$arrayElemAt : ['$metrica.frame',0]}, null]},\n"
			+ "            slot:{$ifNull:[{$arrayElemAt : ['$metrica.slot',0]}, null]},\n"
			+ "            port:{$ifNull:[{$arrayElemAt : ['$metrica.port',0]}, null]}\n"
			+ "        }\n"
			+ " }\n"
			, "{$unset:[\"metrica\", '_id']}\n"
			, "{$out: 'tb_inventario_onts_tmp_nce'}\n"
			})
	List<InventarioAuxTransEntity> getFrameSlotPort(@Param("idRegion") Integer idRegion, @Param("idOLt") Integer idOLt);
	
	
	@Aggregation(pipeline = { 
			"{$unset:['_id']}"
			,"{ $merge: { into: \"tb_inventario_onts_descubrimiento_nce\", on: \"_id\", whenMatched: \"replace\", whenNotMatched: \"insert\" } }"
	})
	void outToInv();
	
	
}
