package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;

@Document(collection = "tb_configuracion_metricas")
@Data
@NoArgsConstructor
public class ConfiguracionMetricaEntity {
	@Id
	private String _id;
	private Integer id_metrica;
	private String nombre;
	private boolean activo;
	private String tiempo;
	private Integer[] bloque;
	private Integer[] id_configuracion;	
	private CadenasMetricasDto HUAWEI;
	private CadenasMetricasDto ZTE;
	private CadenasMetricasDto FH;

}
