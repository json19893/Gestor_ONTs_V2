package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;
import totalplay.snmpv2.com.persistencia.entidades.AuxiliarEstatusEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioAuxTransEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

import java.util.List;

public interface IauxiliarEstatusRepository extends MongoRepository<AuxiliarEstatusEntity, String> {
	@Aggregation(pipeline = { 
		      "{$out:'tb_inventario_onts'}"
	  		})
	void sendToInventario();

}
