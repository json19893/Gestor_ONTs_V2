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
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsDescubrimientoNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioPuertosEntity;

public interface IinventarioOntsDescubrimientoNCERepository extends MongoRepository<InventarioOntsDescubrimientoNCEEntity, String> {
	
	@Query(value="{'id_ejecucion' : ?0}", delete = true)
	void deleteEjecucion(@Param("ejecucion") String ejecucion);
}
