package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.negocio.dto.listInventarioOntsDto;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;

@Repository
public interface IinventarioOltsReposirorio extends MongoRepository<inventarioOntsEntidad, String> {
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{'$match':{'$and':[{'id_olt':?0},{'estatus':?1}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOlts(@Param("idOlts") Integer idOlts, @Param("status") Integer status);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'id_olt':?0}}" })
	List<inventarioOntsEntidad> finOntsByIdAll(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'id_olt':?0}}" })
	List<listInventarioOntsDto> finOntsByIdAllService(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{'$match':{'$and':[{'id_olt':?0},{'tipo': 'E'}]} }" })
	List<inventarioOntsEntidad> finOntsByIdAllEmp(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'$and':[{'id_olt':?0},{'vip': 1}]} }" })
	List<inventarioOntsEntidad> finOntsByIdAllVips(@Param("idOlts") Integer idOlts);

	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'$and':[{'id_olt':?0},{sa: true}]} }" })
	List<inventarioOntsEntidad> finOntsByIdAllSa(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'$and':[{'id_olt':?0},{'tipo': 'E'}]} }" })
	List<listInventarioOntsDto> finOntsByIdAllEmpService(@Param("idOlts") Integer idOlts);
	
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{'$match':{'$and':[{'id_olt':?0},{'estatus':?1}]}}" })
	List<inventarioOntsEntidad> totalByEstatus(@Param("idOlts") Integer idOlts,@Param("estatus") Integer estatus);

	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{'$match':{'$and':[{'id_olt':?0},{'estatus':?1},{'tipo':'E'}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOltsEmp(@Param("idOlts") Integer idOlts, @Param("status") Integer status);

	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{ $match:{$and:[{id_olt:?0},{estatus:?1},{sa:true}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOltsSa(@Param("idOlts") int idOlts, @Param("status") int status);

	@Aggregation(pipeline = {"{$unionWith: 'tb_inventario_onts_pdm'}", "{'$match':{'$and':[{'id_olt':?0},{'estatus':{$in:[2,0]}},{'tipo':?1}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOltsEmpDown(@Param("idOlts") Integer idOlts,@Param("tipo") String tipo);

	@Aggregation(pipeline = {"{$unionWith: {coll:'tb_inventario_onts_pdm', pipeline:[{$match:{sa:true}}]}}", "{'$match':{'$and':[{'id_olt':?0},{'estatus':{$in:[2,0]}},{sa:true}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOltsDownSa(@Param("idOlts") Integer idOlts);
	@Aggregation(pipeline = { "{$unionWith: 'tb_inventario_onts_pdm'}","{'$match':{'$and':[{'id_olt':?0},{'estatus':?1},{'vip':1}]}}" })
	List<inventarioOntsEntidad> finOntsByIdOltsVips(@Param("idOlts") Integer idOlts, @Param("status") Integer status);
}
