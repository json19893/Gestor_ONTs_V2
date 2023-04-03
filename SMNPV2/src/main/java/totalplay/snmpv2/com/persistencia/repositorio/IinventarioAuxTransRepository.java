package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioAuxTransRepository extends MongoRepository<InventarioAuxTransEntity, String> {
	

	@Aggregation(pipeline = { 
		      "{$out:'tb_inventario_onts_aux'}"
	  		})
	void outToInvAux();
	
	@Aggregation(pipeline = { 
		      "{$out:'tb_inventario_onts_aux_manual'}"
	  		})
	void outToInvAuxManual();
	


}


