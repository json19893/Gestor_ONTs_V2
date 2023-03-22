package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_inventario_onts_pdm")
@Data
@NoArgsConstructor
public class InventarioOntsPdmEntity extends GenericPoleosDto {
	
	private String alias;
	private String tipo;
	private String lastDownTime;
	private String descripcionAlarma;
	private Integer actualizacion;
	private Integer vip;
	private String fecha_actualizacion;
	
}
