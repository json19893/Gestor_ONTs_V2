package totalplay.snmpv2.com.persistencia.vertica.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;


public interface ICatAccesoDirectoRepository extends JpaRepository<BmsGestorOraVerticaEntity, Long> {
	
	@Query(value = "select MAX(ONTID) as ONTID, MAX(CIA) as CIA, MAX(NOMBREONT) as NOMBREONT, MAX(FRAME) as FRAME, MAX(NOMBREOLT) as NOMBREOLT, MAX(ETIQUETAONT) as ETIQUETAONT, MAX(EQUIPMENTID) as EQUIPMENTID, MAX(SLOT) as SLOT, MAX(IPOLT) as IPOLT, MAX(STATUS) as STATUS, MAX(CREATETIME) as CREATETIME, MAX(PORT) as PORT, SN, null as USERVLAN \r\n"
			+ "						FROM adquisicion.bms_gestor_ora\r\n"
			//+ "						where SN = '5A544547CE43F9E0'\r\n"
			+ "						GROUP BY SN" , nativeQuery = true)	
	List<BmsGestorOraVerticaEntity> getUniqueSerial();

	
}
