package totalplay.snmpv2.com.persistencia.vertica.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.CatOltsVerticaEntity;


public interface ICatOltsVerticaRepository extends JpaRepository<CatOltsVerticaEntity, Long> {
	
	@Query(value = "select DISTINCT IPOLT, NOMBREOLT from adquisicion.bms_gestor_ora" , nativeQuery = true)	
	List<CatOltsVerticaEntity> getUniqueIps();
}
