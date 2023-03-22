package totalplay.snmpv2.com.negocio.dto;

import java.io.BufferedReader;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.persistencia.entidades.CatConfiguracionEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;

@Data
@NoArgsConstructor
public class OntsConfiguracionDto {
	@Id
	private String _id;
	private InventarioOntsEntity ont;
	private CatConfiguracionEntity configuracion;
	private String ip;
	private String tecnologia;
	private Integer id_configuracion;
	private boolean error;
	private Integer poleable;
}
