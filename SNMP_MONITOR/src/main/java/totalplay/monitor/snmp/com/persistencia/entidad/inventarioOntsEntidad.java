package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.negocio.dto.GenericPoleosDto;


@Document(collection = "tb_inventario_onts")
@Data
@NoArgsConstructor
public class inventarioOntsEntidad  extends GenericPoleosDto {
	
	private String alias;
	private String tipo;
	private String lastDownTime;
	private String descripcionAlarma;
	private Integer actualizacion;
	private Integer vip;
	private boolean sa;
	
	

}