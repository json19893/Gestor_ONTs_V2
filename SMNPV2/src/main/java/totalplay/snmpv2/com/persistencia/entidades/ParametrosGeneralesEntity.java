package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_configuraciones_generales")
@Data
@NoArgsConstructor
public class ParametrosGeneralesEntity {
	@Id
	private String id;
	private Integer id_configuracion;
	private Integer bloque;
	private boolean poleo_metrica;
	private boolean snmp_bulk_walk;
	private boolean reproceso_empresariales;
	private Date inicio_nce;
	private Date fin_nce;
	private Integer olt_actualizada;
	

}
