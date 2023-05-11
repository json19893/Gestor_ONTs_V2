package totalplay.snmpv2.com.persistencia.vertica.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsViejoGestorEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.CatOltsVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.OntsViejoGestorEntity;


public interface IOntsViejoGestorRepository extends JpaRepository<OntsViejoGestorEntity, Long> {
	
	@Query(value = "select id_ont, num_serie, ip_olt, frame, slot, puerto, run_status, last_down_time, last_down_cause  from mod_adhoc_itsm_1.valor_metricas_onts_actual" , nativeQuery = true)	
	List<OntsViejoGestorEntity> getOnts();
}
