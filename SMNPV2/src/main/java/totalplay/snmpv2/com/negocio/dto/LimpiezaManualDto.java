package totalplay.snmpv2.com.negocio.dto;

import java.io.BufferedReader;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasEntity;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.inventarioOntsErroneas;

@Data
@NoArgsConstructor
public class LimpiezaManualDto {
	private List<DiferenciasEntity> duplicados;
	private List<InventarioOntsEntity> eliminar;
	private List<DiferenciasManualEntity> manual;
	private List<InventarioOntsAuxManualEntity> inventarioAux ;
	
}
