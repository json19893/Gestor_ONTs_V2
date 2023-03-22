package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasEntity;
import totalplay.snmpv2.com.persistencia.entidades.FaltantesMetricasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IfaltantesEstatusRepository extends MongoRepository<FaltantesEstatusEntity, String> {
	
	@Aggregation(pipeline = { 
		      "{$unset:['_id'] }"
	  		})
	List<OntsConfiguracionDto> getAll();
	
	
	

}


