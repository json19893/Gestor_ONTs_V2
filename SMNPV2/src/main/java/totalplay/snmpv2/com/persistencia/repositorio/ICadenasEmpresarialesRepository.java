package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.FaltantesDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.persistencia.entidades.AuxiliarJoinEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.CadenasOntsEmpresarialesEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsRespNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

import java.util.List;
import java.util.Optional;

public interface ICadenasEmpresarialesRepository extends MongoRepository<CadenasOntsEmpresarialesEntity, String> {

		
	/**
	 * Obtiene una ont a traves de un numero de serie
	 * @param numSerie
	 * @return InventarioOntsEntity - Representa la ont filtrada por el numero de serie.
	 */
	@Query("{numero_serie: ?0}")
	CadenasOntsEmpresarialesEntity getOntBySerialNumber(String numSerie);
	
	
	@Aggregation(pipeline = { "{$project:{?0:1, _id:0}}"})
	List<String> getOntsByMetrica(@Param("metrica") String metrica);
	
	@Aggregation(pipeline = { 
			  "{\n"
			+ "		\"$lookup\":{\n"
			+ "			from: \"tb_inventario_onts\",\n"
			+ "			localField:\"numero_serie\",\n"
			+ "			foreignField:\"numero_serie\",\n"
			+ "			pipeline:[\n"
			+ "			    {$match:{tipo:\"E\"}}\n"
			+ "			],\n"
			+ "			as: \"onts\",\n"
			+ "		}\n"
			+ "}\n"
			, "{$match:{onts:{$ne:[]}}}\n"
			, "{$unset:\"onts\"}\n"
			, "{$out:\"tb_cadenas_onts_empresariales\"}"})
	void deleteEmpresariales();
	
	
	
	
	
}
